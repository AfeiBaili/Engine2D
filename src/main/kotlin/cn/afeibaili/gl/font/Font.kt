package cn.afeibaili.gl.font


/**
 * # 字体类
 *
 * @author AfeiBaili
 * @version 2026/7/8 18:03
 */

class Font(
    val fontName: String,
    val fontPath: String,
    val defaultSize: Int,
    val ascent: Float,
    val descent: Float,
    val lineHeight: Float,
    val asciiAtlas: AsciiAtlas,
) {
    val texture get() = asciiAtlas.texture

    fun getChar(char: Char): Character? {
        return asciiAtlas.asciiMap[char]
    }

    fun getStringWidth(string: String, scale: Float): Float {
        var width = 0f
        string.forEach { char ->
            val character: Character = getChar(char) ?: return@forEach
            width += character.advanceX * scale
        }
        return width
    }

    fun getStringHeight(scale: Float): Float = lineHeight * scale
}