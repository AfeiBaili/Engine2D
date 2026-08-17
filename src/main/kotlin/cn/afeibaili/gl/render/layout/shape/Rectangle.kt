package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting


/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

class Rectangle(val key: String, override var container: Layout) : AbstractComponent() {
    override var weight: Float = 0f
}

fun Layout.rectangle(setting: Setting = Setting(), key: String): Rectangle {
    return Rectangle(key, this).also {
        setting.apply(it)
        append(it)
    }
}