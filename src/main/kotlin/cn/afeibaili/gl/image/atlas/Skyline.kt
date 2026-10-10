package cn.afeibaili.gl.image.atlas

import cn.afeibaili.gl.image.ImageUtil.extendPixel
import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.util.TempFileUtil
import java.awt.image.BufferedImage
import java.io.File


/**
 * # 天际线图集算法
 *
 * @author AfeiBaili
 * @version 2026/9/24 00:05
 */

class Skyline(override val key: String, val extendPixel: Int = 0) : BigImageAtlas {
    var boxWidth = 0
    var boxHeight = 0
    override val images = mutableListOf<Image>()
    override val imageMap = mutableMapOf<String, Image>()
    val lines = mutableListOf<Line>()
    private val logger = LoggerFactory.create("SkylineAtlas")

    override fun toTexture() = toTexture(false)

    override fun apply() {
        boxWidth = 0
        boxHeight = 0
        lines.clear()
        val sortedImage: List<Image> = images.sortedBy { -(it.width * it.height) }
        images.clear()
        imageMap.clear()
        sortedImage.forEach { place(it) }
    }

    override fun generateUv() {
        logger.debug("$key generate uv...")
        for (image in images) {
            image.uv[0] = (image.atlasX.toFloat() + extendPixel) / boxWidth.toFloat()
            image.uv[1] = (image.atlasY.toFloat() + extendPixel) / boxHeight.toFloat()
            image.uv[2] = (image.atlasX.toFloat() + image.width - (extendPixel * 2)) / boxWidth.toFloat()
            image.uv[3] = (image.atlasY.toFloat() + image.height - (extendPixel * 2)) / boxHeight.toFloat()
        }
    }

    fun toTexture(flip: Boolean = false): Texture {
        val image = BufferedImage(boxWidth, boxHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.graphics
        images.forEach { it -> graphics.drawImage(it.bufferedImage, it.atlasX, it.atlasY, null) }

        if (flip) {
            val flipImage = BufferedImage(boxWidth, boxHeight, BufferedImage.TYPE_INT_ARGB)
            for (y in image.height - 1 downTo 0) {
                for (x in 0..image.width - 1) {
                    val rgb: Int = image.getRGB(x, y)
                    flipImage.setRGB(x, (image.height - 1) - y, rgb)
                }
            }
            graphics.dispose()
            val file: File = TempFileUtil.createTempImageFile(flipImage, "${key}-skyline.png")
            logger.debug("create skyline atlas, file: ${file.absolutePath}")
            return Texture(flipImage)
        }
        val file: File = TempFileUtil.createTempImageFile(image, "${key}-skyline.png")
        logger.debug("create skyline atlas, file: ${file.absolutePath}")
        return Texture(image)
    }

    fun clear() {
        boxWidth = 0
        boxHeight = 0
        images.clear()
        imageMap.clear()
        lines.clear()
    }

    fun add(image: Image) {
        images.add(image)
        imageMap[image.key] = image
    }

    private fun place(image: Image) {
        image.extendPixel(extendPixel)
        images.add(image)
        imageMap[image.key] = image

        if (images.isEmpty()) {
            lines.add(Line(0, image.height, image.width))
            boxWidth = image.width
            boxHeight = image.height
            return
        }

        if (boxHeight + image.height > boxWidth) {
            expandAndPlace(image)
            return
        }

        val findLine: Line? = findSkyline(image)
        if (findLine != null) {
            lines.remove(findLine)
            lines.add(Line(findLine.x, findLine.y + image.height, image.width))
            if (findLine.length > image.width) lines.add(
                Line(
                    findLine.x + image.width,
                    findLine.y,
                    findLine.length - image.width
                )
            )
            if (findLine.y + image.height > boxHeight) boxHeight = findLine.y + image.height
            setImage(findLine.x, findLine.y, image)
            return
        }

        val multipleSkyline: List<MultipleLine> = findMultipleSkyline(image)

        if (multipleSkyline.isNotEmpty()) {
            val multipleLine: MultipleLine = multipleSkyline.minByOrNull { it.lineY }!!
            val lineY: Int = multipleLine.lineY
            val lineX: Int = multipleLine.lineX
            val length: Int = multipleLine.length
            val findLines: List<Line> = multipleLine.lines
            val lastLine: Line = findLines.last()
            lines.removeAll(findLines)
            if (lineY + image.height > boxHeight) boxHeight = lineY + image.height
            setImage(lineX, lineY, image)
            lines.add(
                Line(lineX, lineY + image.height, image.width)
            )

            if (length != image.width) {
                val lastLength = (lastLine.x + lastLine.length) - (lineX + image.width)
                lines.add(
                    Line(lineX + image.width, lastLine.y, lastLength)
                )
            }
            return
        }

        expandAndPlace(image)
    }

    private fun expandAndPlace(image: Image) {
        lines.add(Line(boxWidth, image.height, image.width))
        boxWidth += image.width
        if (image.height > boxHeight) boxHeight = image.height
        setImage(boxWidth - image.width, 0, image)
    }

    private fun findMultipleSkyline(image: Image): List<MultipleLine> {
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

            if (length >= image.width) {
                mutableList.add(MultipleLine(lineX, lineY, length, lineList))
                lineX = 0
                lineY = 0
                length = 0
                lineList = mutableListOf()
            }
        }

        return mutableList
    }

    private fun findSkyline(image: Image): Line? {
        val mapNotNull: List<Line> = lines.mapNotNull { if (it.length > image.width) it else null }
        if (mapNotNull.isEmpty()) return null
        return mapNotNull.sortedBy { -it.length }.sortedBy { it.y }[0]
    }

    private fun setImage(x: Int, y: Int, image: Image) {
        image.atlasX = x
        image.atlasY = y
    }


    class Line(val x: Int, val y: Int, val length: Int)

    class MultipleLine(val lineX: Int, val lineY: Int, val length: Int, val lines: List<Line>)
}