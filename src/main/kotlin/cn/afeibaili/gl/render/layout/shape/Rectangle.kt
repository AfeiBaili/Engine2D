package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout


/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

class Rectangle(val key: String, override var container: Layout) : AbstractComponent() {
    override var weight: Float = 0f
}

fun Layout.rectangle(key: String): Rectangle {
    return Rectangle(key, this).also { append(it) }
}