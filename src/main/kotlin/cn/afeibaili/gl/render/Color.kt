package cn.afeibaili.gl.render

import java.nio.ByteBuffer

/**
 * # 颜色
 *
 * @author AfeiBaili
 * @version 2026/08/19 18:40
 */
class Color(val red: UByte, val green: UByte, val blue: UByte, val alpha: UByte) {
    fun getRed() = red.toByte()
    fun getGreen() = green.toByte()
    fun getBlue() = blue.toByte()
    fun getAlpha() = alpha.toByte()

    override fun toString(): String {
        return "r:$red g:$green b:$blue a:$alpha"
    }

    fun get(buffer: ByteBuffer) {
        buffer.put(getRed()).put(getGreen()).put(getBlue()).put(getAlpha())
    }

    companion object {
        val WHITE = parse("#FFFFFF")
        val BLACK = parse("#000000")
        val RED = parse("#D5281A")
        val GREEN = parse("#6AAB73")
        val BLUE = parse("#559DB2")
        val NONE = parse("#00000000")

        fun parse(text: String): Color {
            val removePrefix: String = text.removePrefix("#")
            if (removePrefix.length !in 6..8) throw IllegalArgumentException("无效的颜色序号: 原因长度大于8, 当前长度${removePrefix.length}")
            val bytes: List<UByte> = runCatching {
                removePrefix.chunked(2).map { it.toInt(16).toUByte() }
            }.getOrElse {
                throw IllegalArgumentException("无法解析的颜色: $text")
            }

            val color = when (bytes.size) {
                3 -> Color(bytes[0], bytes[1], bytes[2], UByte.MAX_VALUE)
                4 -> Color(bytes[0], bytes[1], bytes[2], bytes[3])
                else -> throw IllegalArgumentException("无效的颜色值")
            }

            return color
        }

        fun parse(red: Float, green: Float, blue: Float, alpha: Float = 1f): Color {
            val bytes: List<UByte> = mutableListOf(red, green, blue, alpha).map {
                if (it !in 0f..1f) throw IllegalArgumentException("色值不在0-1之间: $it")
                lerp(it, UByte.MIN_VALUE.toInt(), UByte.MAX_VALUE.toInt())
            }
            return Color(bytes[0], bytes[1], bytes[2], bytes[3])
        }

        private fun lerp(value: Float, min: Int, max: Int): UByte {
            return (min + value * (max - min)).toUInt().toUByte()
        }
    }
}