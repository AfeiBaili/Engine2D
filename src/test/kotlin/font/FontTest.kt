package font

import cn.afeibaili.gl.util.memoryStack
import org.lwjgl.PointerBuffer
import org.lwjgl.util.freetype.*
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.nio.ByteBuffer
import javax.imageio.ImageIO
import kotlin.test.Test

/**
 * # 字体信息测试
 *
 * @author AfeiBaili
 * @version 2026/8/24 22:59
 */

class FontTest {
    val fontPath = "B:\\Java\\Kotlin\\JumpJump\\resource\\font\\SourceHanSansHWSC-Regular.otf"
    val fontSize = 64
    val text = "Hello FreetypegGx, 你好吗？"

    @Test
    fun testImage() {
        val ftp: Long = memoryStack { it ->
            val pointer: PointerBuffer = it.mallocPointer(1)
            check(FreeType.FT_Init_FreeType(pointer) == 0) { "初始化失败" }
            pointer.get(0)
        }

        val face: FT_Face = memoryStack { it ->
            val pointer: PointerBuffer = it.mallocPointer(1)
            val newFace: Int = FreeType.FT_New_Face(ftp, fontPath, 0, pointer)
            check(newFace == 0) { "无法加载字体" }
            val face: FT_Face = FT_Face.create(pointer.get(0))
            FreeType.FT_Set_Pixel_Sizes(face, 0, fontSize)
            face
        }

        val metrics: FT_Size_Metrics = face.size()!!.metrics()
        val ascend: Float = metrics.ascender() / 64f
        val descend: Float = metrics.descender() / 64f
        val lineHeight: Float = metrics.height() / 64f

        val cellWidth = lineHeight.toInt()
        val imageWidth = cellWidth * text.length
        val image = BufferedImage(imageWidth, lineHeight.toInt(), BufferedImage.TYPE_INT_ARGB)
        val graphics: Graphics = image.graphics

        var currentCellIndex = 0
        text.forEach { char ->
            val (character, buffer) = loadChar(char, face).toPair()
            if (buffer == null) {
                currentCellIndex++
                return@forEach
            }
            val drawCurrentCellX: Float = (currentCellIndex * cellWidth) + cellWidth / 2 - character.advanceX / 2
            val drawCurrentCellY = ascend.toInt() - character.bearingY

            draw@ for (y in 0 until character.height) {
                for (x in 0 until character.width) {
                    val index = (y * character.width + x)
                    val gray = (buffer.get(index)).toInt() and 0xFF
                    val argb = (gray shl 24) or 0x00000000
                    runCatching {
                        image.setRGB(drawCurrentCellX.toInt() + x, drawCurrentCellY + y, argb)
                    }.onFailure { continue@draw }
                }
            }
            currentCellIndex++
        }

        ImageIO.write(image, "PNG", File("B:\\Java\\Kotlin\\JumpJump\\temp\\test-font.png"))
        graphics.dispose()
    }

    fun loadChar(char: Char, face: FT_Face): CharData {
        val loadedChar: Int = FreeType.FT_Load_Char(face, char.code.toLong(), FreeType.FT_LOAD_RENDER)
        check(loadedChar == 0) { "找不到字符" }
        val glyphSlot: FT_GlyphSlot? = face.glyph()
        glyphSlot ?: error("无法加载字形: $char")
        val bitmap: FT_Bitmap = glyphSlot.bitmap()
        val width: Int = bitmap.pitch()
        val height: Int = bitmap.rows()
        val advanceX: Float = glyphSlot.advance().x() / 64f
        val bearingY: Int = glyphSlot.bitmap_top()
        val bearingX: Int = glyphSlot.bitmap_left()
        val buffer: ByteBuffer? = bitmap.buffer(width * height)
        return CharData(Character(char, width, height, bearingX, bearingY, advanceX), buffer)
    }

    class CharData(val character: Character, val buffer: ByteBuffer?) {
        fun toPair(): Pair<Character, ByteBuffer?> = Pair(character, buffer)
    }
}