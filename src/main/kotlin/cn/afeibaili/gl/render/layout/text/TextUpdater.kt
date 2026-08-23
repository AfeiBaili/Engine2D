package cn.afeibaili.gl.render.layout.text


/**
 * # 文本更新器
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:18
 */

class TextUpdater {
    val map = mutableMapOf<String, Text>()

    fun put(text: Text) {
        map.put(text.key, text)
    }

    fun update(key: String, text: String) {
        val textObj: Text = map[key] ?: return
        textObj.string = text
        val stringHeight: Float = textObj.font.getStringHeight(text, textObj.scale)
        val stringWidth: Float = textObj.font.getStringWidth(text, textObj.scale)
        textObj.width = stringWidth
        textObj.height = stringHeight
        textObj.background.apply {
            width = stringWidth
            height = stringHeight
        }
    }

    operator fun get(key: String): Text? {
        return map[key]
    }

    fun forEach(action: (Text) -> Unit) {
        map.values.forEach { (action(it)) }
    }
}