package cn.afeibaili.gl.render.layout.fixed

import cn.afeibaili.gl.render.layout.Layout

class FixedLayout(override var container: Layout) : AbstractFixedLayout() {
    override fun update() {
    }
}

fun Layout.fixed(action: Layout.() -> Unit): Layout {
    val layout = FixedLayout(this)
    +layout

    action(layout)
    layout.update()
    return layout
}