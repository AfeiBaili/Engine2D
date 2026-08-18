package cn.afeibaili.gl.render.layout.fixed

import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting

class FixedLayout(override var container: Layout) : AbstractFixedLayout()

fun Layout.fixed(setting: (Setting) -> Unit = {}, action: Layout.() -> Unit): Layout {
    val layout = FixedLayout(this)
    val set = Setting()
    setting(set)
    set.apply(layout)
    +layout

    action(layout)
    return layout
}