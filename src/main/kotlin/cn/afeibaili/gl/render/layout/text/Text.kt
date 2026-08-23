package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.font.Font
import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.shape.Rectangle
import cn.afeibaili.gl.render.layout.shape.rectangle

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
    val color: Color,
    val updater: TextUpdater,
    val background: Rectangle,
    override var container: Layout,
) : AbstractComponent()

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
) {
    val stringWidth: Float = font.getStringWidth(string, scale)
    val stringHeight: Float = font.getStringHeight(string, scale)
    val rectangle: Rectangle = rectangle({ it.size(stringWidth, stringHeight).backgroundColor(backgroundColor) }, key)
    val text = Text(key, string, scale, font, color, updater, rectangle, this)
    this.append(text)
    text.relativeX = x
    text.relativeY = y
    text.width = font.getStringWidth(text.string, text.scale)
    text.height = font.getStringHeight(text.string, text.scale)

    updater.put(text)
}