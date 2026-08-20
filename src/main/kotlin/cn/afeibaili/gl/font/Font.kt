package cn.afeibaili.gl.font


/**
 * # 字体类
 *
 * @author AfeiBaili
 * @version 2026/7/8 18:03
 */

class Font(val fontName: String, val fontPath: String, val defaultSize: Int, val asciiAtlas: AsciiAtlas) {
    val texture get() = asciiAtlas.texture

    /**
     * uv结构
     *
     *  索引0为起始x点
     *
     *  索引1为起始y点
     *
     *  索引2为终止x点
     *
     *  索引3为终止y点
     */
    fun getChar(char: Char): Character? {
        return asciiAtlas.asciiMap[char]
    }
}