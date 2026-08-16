package cn.afeibaili.gl.render.layout.fixed

import cn.afeibaili.gl.render.layout.Layout

class FixedLayout(override var container: Layout) : AbstractFixedLayout() {
    override fun update() {
        items.forEach { if (it is Layout) it.update() }
    }
}

fun Layout.fixed(action: Layout.() -> Unit): Layout {
    val layout = FixedLayout(this)
    +layout

    action(layout)
    return layout
}