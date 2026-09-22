import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

/**
 * # 矩形盒子测试
 *
 * @author AfeiBaili
 * @version 2026/9/8 13:31
 */
class TestRectBox {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var isRunning = true
            val atlas = Atlas()

            Thread {
                while (isRunning) {
                    val text: String = readln()
                    if (text.equals("stop", ignoreCase = true)) isRunning = false
                    if (text.equals("add", ignoreCase = true)) {
                        atlas.add(randomImage().asImage())
                        atlas.draw()
                    }
                    if (text.equals("add10", ignoreCase = true)) {
                        repeat(10) { atlas.add(randomImage().asImage()) }
                        atlas.draw()
                    }
                    if (text.equals("add100", ignoreCase = true)) {
                        repeat(100) { atlas.add(randomImage().asImage()) }
                        atlas.draw()
                    }
                }
            }.apply { isDaemon = true;start() }

            while (isRunning) {
                randomImage().write(File("B:\\Java\\Kotlin\\Engine2D\\temp\\random.image.png"))
            }
        }
    }
}

class Atlas() {
    val list = mutableListOf<Image>()
    val spaces = mutableListOf<Image.Space>()
    var containerWidth = 0
    var containerHeight = 0

    fun add(image: Image) {
        list.add(image)

        val findSpace: Image.Space? =
            spaces.find { space -> space.width >= image.width && space.height >= image.height }
        if (findSpace != null) {
            if (findSpace.innerBlock != null) {
                val block: Image.Space.Block = findSpace.innerBlock
                val space1Width = findSpace.width
                val space1Height = findSpace.height - block.height
                val space2Width = findSpace.width - block.width
                val space2Height = findSpace.height

                if (space1Width >= image.width && space1Height >= image.height) {
                    image.x = findSpace.x
                    image.y = findSpace.y + block.height

                    spaces += Image.Space(
                        findSpace.x, findSpace.y + block.height, space1Width, space1Height,
                        Image.Space.Block(findSpace.x, findSpace.y + block.height, image.width, image.height)
                    )
                    spaces += Image.Space(
                        findSpace.x + block.width,
                        findSpace.y,
                        findSpace.width - block.width,
                        findSpace.height - block.height,
                    )
                    spaces.remove(findSpace)
                    return
                } else if (space2Width >= image.width && space2Height >= image.height) {
                    image.x = findSpace.x + block.width
                    image.y = findSpace.y

                    spaces += Image.Space(
                        findSpace.x + block.width, findSpace.y, space2Width, space2Height,
                        Image.Space.Block(findSpace.x + block.width, findSpace.y, image.width, image.height)
                    )

                    spaces += Image.Space(
                        findSpace.x,
                        findSpace.y + block.height,
                        findSpace.width - block.width,
                        findSpace.height - block.height
                    )
                    spaces.remove(findSpace)
                    return
                }
            } else {
                image.x = findSpace.x
                image.y = findSpace.y
                spaces += Image.Space(
                    findSpace.x, findSpace.y, findSpace.width, findSpace.height,
                    Image.Space.Block(image.x, image.y, image.width, image.height)
                )
                spaces.remove(findSpace)
                return
            }
        }
        //如果 宽大于高
        if (containerWidth > containerHeight) {
            image.x = 0
            image.y = containerHeight
            if (image.width > containerWidth) {
                //溢出
                spaces += Image.Space(
                    containerWidth,
                    0,
                    image.width - containerWidth,
                    containerHeight
                )
                containerWidth = image.width
                containerHeight += image.height
            } else {
                spaces += Image.Space(
                    image.width,
                    containerHeight,
                    containerWidth - image.width,
                    image.height
                )
                containerHeight += image.height
            }
        } else {
            image.x = containerWidth
            image.y = 0
            if (image.height > containerHeight) {
                //溢出
                spaces += Image.Space(
                    0,
                    containerHeight,
                    containerWidth,
                    image.height - containerHeight
                )
                containerWidth += image.width
                containerHeight = image.height
            } else {
                spaces += Image.Space(
                    containerWidth,
                    image.height,
                    image.width,
                    containerHeight - image.height
                )
                containerWidth += image.width
            }
        }
    }

    fun draw() {
        val image = BufferedImage(containerWidth, containerHeight, BufferedImage.TYPE_INT_ARGB)
        list.forEach { it ->
            image.graphics.drawImage(it.buffer, it.x, it.y, null)
        }
        ImageIO.write(image, "png", File("B:\\Java\\Kotlin\\Engine2D\\temp\\box-atlas.png"))
    }
}

class Image(width: Int, height: Int) {
    constructor(buffer: BufferedImage) : this(buffer.width, buffer.height) {
        this.buffer = buffer
    }

    var buffer = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    var x = 0
    var y = 0
    val width get() = buffer.width
    val height get() = buffer.height


    class Space(
        val x: Int, val y: Int, val width: Int, val height: Int,
        val innerBlock: Block? = null,
    ) {
        class Block(val x: Int, val y: Int, val width: Int, val height: Int)
    }

    override fun toString(): String {
        return "y=$y,x=$x,width=$width,height=$height"
    }
}

fun randomImage(): BufferedImage {
    val random = Random()
    random.nextInt(50)
    val width = random.nextInt(16, 64)
    val height = random.nextInt(16, 64)
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics: Graphics = image.graphics
    graphics.color = randomColor()
    graphics.fillRect(0, 0, width, height)
    graphics.dispose()
    return image
}

fun BufferedImage.asImage(): Image {
    return Image(this)
}

fun randomColor(): Color {
    val random = Random()
    return Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

fun BufferedImage.write(file: File) {
    ImageIO.write(this, "png", file)
}

