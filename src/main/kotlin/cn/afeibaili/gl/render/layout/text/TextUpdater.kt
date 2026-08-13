package cn.afeibaili.gl.render.layout.text


/**
 * # 文本更新器
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:18
 */

class TextUpdater {
    val list = mutableMapOf<String, Text>()

    fun put(key: String, text: Text) {
        list.put(key, text)
    }

    operator fun get(key: String): Text? {
        return list[key]
    }

    fun update(key: String, text: String) {
        val t: Text? = list[key]
        t?.text = text
    }
}