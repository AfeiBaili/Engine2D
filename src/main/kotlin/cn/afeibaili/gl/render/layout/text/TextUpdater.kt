package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.render.layout.Updatable


/**
 * # 文本更新器
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:18
 */

class TextUpdater : Updatable<Text> {
    override val map = mutableMapOf<String, Text>()

    fun put(value: Text) {
        map.put(value.key, value)
    }

    fun update(key: String, value: String) {
        val textObj: Text = map[key] ?: return
        textObj.string = value
        val stringHeight: Float = textObj.font.getStringHeight(textObj.scale)
        val stringWidth: Float = textObj.font.getStringWidth(value, textObj.scale)
        textObj.width = stringWidth
        textObj.height = stringHeight
    }

    operator fun get(key: String): Text? {
        return map[key]
    }

    fun forEach(handler: (Text) -> Unit) {
        map.values.forEach { (handler(it)) }
    }
}