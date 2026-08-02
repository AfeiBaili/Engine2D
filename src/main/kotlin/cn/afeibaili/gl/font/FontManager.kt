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


/**
 * # 字体工厂，创建字体文件
 *
 * @author AfeiBaili
 * @version 2026/7/10 20:06
 */

object FontManager {
    private val logger = LoggerFactory.create("FontManager")

    val freetypePointer: Long = memoryStack { it ->
        val pointer: PointerBuffer = it.mallocPointer(1)
        check(FreeType.FT_Init_FreeType(pointer) == 0) { "FreeType初始化失败" }
        logger.info("freetype is initialized")
        pointer.get(0)
    }

    fun create(fontName: String, filepath: String, fontSize: Int): Font {
        logger.info("create ascii atlas")
        val face: FT_Face = memoryStack { stack ->
            val pointer: PointerBuffer = stack.mallocPointer(1)
            check(
                FreeType.FT_New_Face(freetypePointer, filepath, 0, pointer) == 0
            ) { "无法加载[${fontName}]字体: $filepath" }
            val face: FT_Face = FT_Face.create(pointer[0])
            FreeType.FT_Set_Pixel_Sizes(face, 0, fontSize)
            face
        }

        val border: Int = fontSize * 10
        val charMap = mutableMapOf<Char, Character>()
        val image = BufferedImage(border, border, BufferedImage.TYPE_INT_ARGB)
        var currentX = 0
        var currentY = 0
        var maxAdvance = 0
        var maxHeight = 0

        for (charCode in 32..126) {
            val char: Char = charCode.toChar()
            check(
                FreeType.FT_Load_Char(face, charCode.toLong(), FreeType.FT_LOAD_RENDER) == 0
            ) { "找不到字符: $charCode" }
            val glyph: FT_GlyphSlot? = face.glyph()
            glyph ?: error("字形为空，无法获取字形: $charCode")
            val metrics: FT_Glyph_Metrics = glyph.metrics()
            val advance = metrics.horiAdvance().toInt() shr 6
            if (maxAdvance < advance) maxAdvance = advance
            val bitmap: FT_Bitmap = glyph.bitmap()
            val width = bitmap.width()
            val height = bitmap.rows()
            if (maxHeight < height) maxHeight = height

            val pitch: Int = bitmap.pitch()
            val size = height * pitch
            val buffer: ByteBuffer? = bitmap.buffer(size)
            if (buffer == null) {
                charMap.put(char, Character(char, FloatArray(4), advance.toFloat() / 2, height.toFloat(), 0f, 0f))
                continue
            }
            val bearingTop: Int = (metrics.vertBearingY() shr 6).toInt()

            val x = currentX
            val y = currentY

            if (currentX + width >= border) {
                currentX = 0
                currentY += maxAdvance + maxHeight + 2
            } else {
                currentY += bearingTop
            }
            loop@ for (y in 0 until height) {
                for (x in 0 until width) {
                    val index = (y * pitch + x)
                    val gray = buffer.get(index).toInt() and 0xFF
                    val argb = (gray shl 24) or 0x00FFFFFF
                    runCatching {
                        image.setRGB(currentX + x, currentY + y, argb)
                    }.onFailure { continue@loop }
                }
            }

            val uv = FloatArray(4)
            uv[0] = currentX.toFloat() / border
            uv[1] = currentY.toFloat() / border
            uv[2] = (currentX + width).toFloat() / border
            uv[3] = (currentY + height).toFloat() / border

            currentY -= bearingTop
            currentX += width + 2
            charMap.put(char, Character(char, uv, width.toFloat(), height.toFloat(), x.toFloat(), y.toFloat()))
        }
        ImageIO.write(image, "png", File("${System.getProperty("user.dir")}/temp/${fontName}.png"))

        return Font(AsciiAtlas(charMap, Texture(image)))
    }
}