import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class SkylineBox() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var isRunning = true
            val box = Box()
            println("running...")

            while (isRunning) {
                val text: String = readln()
                if (text.equals("stop", ignoreCase = true)) isRunning = false
                if (text.equals("add", ignoreCase = true)) {
                    box.put(randomRect())
                    box.set()
                    draw(box)
                }
                if ("add[0-9]+".toRegex().containsMatchIn(text)) {
                    val count: Int = "add([1-9]?[0-9]*)".toRegex().find(text)!!.groups[1]!!.value.toInt()
                    repeat(count) { box.put(randomRect()) }
                    box.set()
                    draw(box)
                }
            }
        }

        fun draw(box: Box) {
            val lineHeight = 2

            val sourceImage = BufferedImage(box.boxWidth, box.boxHeight + lineHeight, BufferedImage.TYPE_INT_ARGB)
            val graphics = sourceImage.graphics
            box.rects.forEach { rect -> graphics.drawImage(rect.image, rect.x, rect.y, null) }
            graphics.color = Color.BLACK
            box.lines.forEach { line -> graphics.fillRect(line.x, line.y, line.length, lineHeight) }
            System.out.printf("lines = %s, rects = %s\n", box.lines.size, box.rects.size)

            val targetImage = BufferedImage(sourceImage.width, sourceImage.height, BufferedImage.TYPE_INT_ARGB)

            for (y in sourceImage.height - 1 downTo 0) {
                for (x in 0..sourceImage.width - 1) {
                    val rgb: Int = sourceImage.getRGB(x, y)
                    targetImage.setRGB(x, (sourceImage.height - 1) - y, rgb)
                }
            }
            ImageIO.write(targetImage, "PNG", File("B:\\Java\\Kotlin\\Engine2D\\temp\\box-atlas.png"))
        }

        fun randomRect(): Rect {
            val random = Random()
            val minSize = 16
            val maxSize = 64
            val image = BufferedImage(
                random.nextInt(minSize, maxSize), random.nextInt(minSize, maxSize), BufferedImage.TYPE_INT_ARGB
            )
            val graphics: Graphics = image.graphics
            graphics.color = randomColor()
            graphics.fillRect(0, 0, image.width, image.height)
            graphics.dispose()
            return Rect(image)
        }

        fun randomColor(): Color {
            val random = Random()
            return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        }

        fun asImage(width: Int, height: Int): BufferedImage {
            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            val graphics = image.graphics
            graphics.color = randomColor()
            graphics.fillRect(0, 0, width, height)
            return image
        }
    }


    class Box() {
        var boxWidth: Int = 0
        var boxHeight: Int = 0
        var rects = mutableListOf<Rect>()
        var lines = mutableListOf<Line>()

        fun set() {
            boxWidth = 0
            boxHeight = 0
            lines.clear()
            val sortedRect: List<Rect> = rects.sortedBy { -(it.width * it.height) }
            rects.clear()
            sortedRect.forEach { place(it) }
        }

        fun put(rect: Rect) = rects.add(rect)

        fun place(rect: Rect) {
            rects.add(rect)

            if (rects.isEmpty()) {
                lines.add(Line(0, rect.height, rect.width))
                boxWidth = rect.width
                boxHeight = rect.height
                return
            }

            if (boxHeight + rect.height > boxWidth) {
                expandAndPlace(rect)
                return
            }

            val findLine: Line? = findSkyline(rect)
            if (findLine != null) {
                rect.x = findLine.x
                rect.y = findLine.y
                lines.remove(findLine)
                lines.add(Line(findLine.x, findLine.y + rect.height, rect.width))
                if (findLine.length > rect.width) lines.add(
                    Line(
                        findLine.x + rect.width,
                        findLine.y,
                        findLine.length - rect.width
                    )
                )
                if (findLine.y + rect.height > boxHeight) boxHeight = findLine.y + rect.height
                return
            }

            val multipleSkyline: List<MultipleLine> = findMultipleSkyline(rect)

            if (multipleSkyline.isNotEmpty()) {
                val multipleLine: MultipleLine = multipleSkyline.minByOrNull { it.lineY }!!
                val lineY: Int = multipleLine.lineY
                val lineX: Int = multipleLine.lineX
                val length: Int = multipleLine.length
                val findLines: List<Line> = multipleLine.lines
                val lastLine: Line = findLines.last()
                lines.removeAll(findLines)
                rect.y = lineY
                rect.x = lineX
                if (lineY + rect.height > boxHeight) boxHeight = lineY + rect.height
                lines.add(
                    Line(lineX, lineY + rect.height, rect.width)
                )

                if (length != rect.width) {
                    val lastLength = (lastLine.x + lastLine.length) - (lineX + rect.width)
                    lines.add(
                        Line(lineX + rect.width, lastLine.y, lastLength)
                    )
                }
                return
            }

            expandAndPlace(rect)
        }

        fun findMultipleSkyline(rect: Rect): List<MultipleLine> {
            lines.sortBy { line -> line.x }
            val mutableList = mutableListOf<MultipleLine>()
            var lineX = 0
            var lineY = 0
            var length = 0
            var lineList = mutableListOf<Line>()

            for (line in lines) {
                if (line.y > lineY) {
                    lineX = line.x
                    lineY = line.y
                    length = line.length
                    lineList.clear()
                } else {
                    length += line.length
                }

                lineList.add(line)

                if (length >= rect.width) {
                    mutableList.add(MultipleLine(lineX, lineY, length, lineList))
                    lineX = 0
                    lineY = 0
                    length = 0
                    lineList = mutableListOf()
                }
            }

            return mutableList
        }

        fun expandAndPlace(rect: Rect) {
            lines.add(Line(boxWidth, rect.height, rect.width))
            rect.x = boxWidth
            rect.y = 0
            boxWidth += rect.width
            if (rect.height > boxHeight) boxHeight = rect.height
        }

        fun findSkyline(rect: Rect): Line? {
            val mapNotNull: List<Line> = lines.mapNotNull { if (it.length > rect.width) it else null }
            if (mapNotNull.isEmpty()) return null
            return mapNotNull.sortedBy { -it.length }.sortedBy { it.y }[0]
        }
    }

    class Rect(val image: BufferedImage) {
        var x: Int = 0
        var y: Int = 0
        val width: Int = image.width
        val height: Int = image.height
        override fun toString(): String {
            return "Rect(x=$x, y=$y, width=$width, height=$height)"
        }

    }

    class Line(val x: Int, val y: Int, val length: Int) {
        override fun toString(): String {
            return "Line(x=$x, y=$y, length=$length)"
        }
    }

    class MultipleLine(val lineX: Int, val lineY: Int, val length: Int, val lines: List<Line>)
}