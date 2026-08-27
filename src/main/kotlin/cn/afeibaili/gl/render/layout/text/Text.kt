package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.font.Font
import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.shape.BackgroundRectangle

/**
 * # 文本布局类
 *
 * @author AfeiBaili
 * @version 2026/08/11 11:50
 */

class Text internal constructor(
    val key: String,
    var string: String,
    var scale: Float,
    val font: Font,
    var color: Color,
    val updater: TextUpdater,
    override var container: Layout,
) : AbstractComponent() {
    val backgroundRect: BackgroundRectangle =
        BackgroundRectangle(key, this, container)
}

fun Layout.text(
    key: String,
    string: String,
    font: Font,
    x: Float = 0f,
    y: Float = 0f,
    scale: Float = 1f,
    color: Color = Color.WHITE,
    backgroundColor: Color = Color.NONE,
    updater: TextUpdater,
): Text {
    val stringWidth: Float = font.getStringWidth(string, scale)
    val stringHeight: Float = font.getStringHeight(scale)
    val text = Text(key, string, scale, font, color, updater, this)
    this.append(text)
    text.relativeX = x
    text.relativeY = y
    text.width = stringWidth
    text.height = stringHeight
    text.backgroundColor = backgroundColor

    updater.put(text)
    return text
}