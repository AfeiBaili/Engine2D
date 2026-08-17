package cn.afeibaili.gl.render.layout.fixed

import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting

class FixedLayout(override var container: Layout) : AbstractFixedLayout() {
    override fun update() {
        items.forEach { if (it is Layout) it.update() }
    }
}

fun Layout.fixed(setting: Setting = Setting(), action: Layout.() -> Unit): Layout {
    val layout = FixedLayout(this)
    setting.apply(layout)
    +layout

    action(layout)
    return layout
}