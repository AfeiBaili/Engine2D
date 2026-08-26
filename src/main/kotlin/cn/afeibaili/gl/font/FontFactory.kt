package cn.afeibaili.gl.font

import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.util.memoryStack
import org.lwjgl.PointerBuffer
import org.lwjgl.util.freetype.*
import java.awt.image.BufferedImage
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.sqrt


/**
 * # 字体工厂，创建字体文件
 *
 * @author AfeiBaili
 * @version 2026/7/10 20:06
 */

object FontFactory {
    private val logger = LoggerFactory.create("FontManager")

    val freetypePointer: Long = memoryStack { it ->
        val pointer: PointerBuffer = it.mallocPointer(1)
        check(FreeType.FT_Init_FreeType(pointer) == 0) { "FreeType初始化失败" }
        logger.info("freetype is initialized")
        pointer.get(0)
    }

    fun getFace(fontName: String, filepath: String, defaultSize: Int): FT_Face {
        val face: FT_Face = memoryStack { stack ->
            val pointer: PointerBuffer = stack.mallocPointer(1)
            check(
                FreeType.FT_New_Face(freetypePointer, filepath, 0, pointer) == 0
            ) { "无法加载[${fontName}]字体: $filepath" }
            val face: FT_Face = FT_Face.create(pointer[0])
            FreeType.FT_Set_Pixel_Sizes(face, 0, defaultSize)
            face
        }

        return face
    }

    fun create(fontName: String, filepath: String, defaultSize: Int): Font {
        logger.info("create ascii atlas")
        val face: FT_Face = getFace(fontName, filepath, defaultSize)
        val sizeMetrics: FT_Size_Metrics = face.size()!!.metrics()
        val ascent = sizeMetrics.ascender() / 64f
        val descent = sizeMetrics.descender() / 64f
        val lineHeight = sizeMetrics.height() / 64f

        val cellWidth = lineHeight.toInt()
        val charSize: Int = 127 - 32
        val ceil = ceil(sqrt(charSize.toDouble())).toInt()
        val imageSize = cellWidth * ceil
        val charMap = mutableMapOf<Char, Character>()
        val image = BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB)

        var currentCellIndexX = 0
        var currentCellIndexY = 0
        for (charCode in 32..126) {
            val char = charCode.toChar()
            val charDate = loadChar(char, face)
            if (charDate.buffer == null) {
                charMap.put(
                    char, Character(
                        char,
                        FloatArray(4),
                        0f,
                        0f,
                        0f,
                        0f,
                        charDate.advanceX
                    )
                )
                currentCellIndexX++
                continue
            }

            if ((charCode - 32) % ceil == 0 && (charCode - 32) != 0) {
                currentCellIndexY++
                currentCellIndexX = 0
            }

            val drawCurrentCellX = (currentCellIndexX * cellWidth) + cellWidth / 2 - charDate.advanceX.toInt() / 2
            val drawCurrentCellY = (currentCellIndexY * cellWidth) + ascent.toInt() - charDate.bearingY

            draw@ for (y in 0 until charDate.height) {
                for (x in 0 until charDate.pitch) {
                    val index = (y * charDate.pitch + x)
                    val gray = charDate.buffer.get(index).toInt() and 0xFF
                    val argb = (gray shl 24) or 0x00FFFFFF
                    runCatching {
                        image.setRGB(drawCurrentCellX + x, drawCurrentCellY + y, argb)
                    }.onFailure { continue@draw }
                }
            }

            val uv = FloatArray(4)
            uv[0] = drawCurrentCellX.toFloat() / imageSize
            uv[1] = drawCurrentCellY.toFloat() / imageSize
            uv[2] = (drawCurrentCellX + charDate.width).toFloat() / imageSize
            uv[3] = (drawCurrentCellY + charDate.height).toFloat() / imageSize

            charMap.put(
                char,
                Character(
                    char,
                    uv,
                    charDate.width.toFloat(),
                    charDate.height.toFloat(),
                    charDate.bearingX.toFloat(),
                    charDate.bearingY.toFloat(),
                    charDate.advanceX
                )
            )
            currentCellIndexX++
        }
        ImageIO.write(image, "png", File("${System.getProperty("user.dir")}/temp/${fontName}.png"))

        return Font(fontName, filepath, defaultSize, ascent, descent, lineHeight, AsciiAtlas(charMap, Texture(image)))
    }

    fun loadChar(char: Char, face: FT_Face): CharData {
        val loadedChar: Int = FreeType.FT_Load_Char(face, char.code.toLong(), FreeType.FT_LOAD_RENDER)
        check(loadedChar == 0) { "找不到字符" }
        val glyphSlot: FT_GlyphSlot? = face.glyph()
        glyphSlot ?: error("无法加载字形: $char")
        val bitmap: FT_Bitmap = glyphSlot.bitmap()
        val width: Int = bitmap.width()
        val pitch: Int = bitmap.pitch()
        val height: Int = bitmap.rows()
        val advanceX: Float = glyphSlot.advance().x() / 64f
        val bearingY: Int = glyphSlot.bitmap_top()
        val bearingX: Int = glyphSlot.bitmap_left()
        val buffer: ByteBuffer? = bitmap.buffer(pitch * height)
        return CharData(char, width, height, pitch, bearingX, bearingY, advanceX, buffer)
    }

    class CharData(
        val char: Char,
        val width: Int,
        val height: Int,
        val pitch: Int,
        val bearingX: Int,
        val bearingY: Int,
        val advanceX: Float,
        val buffer: ByteBuffer?,
    )
}