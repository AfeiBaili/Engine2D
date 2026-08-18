package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting

fun Layout.rowWeight(setting: (Setting) -> Unit = {}, action: RowWeight.() -> Unit): Layout {
    val layout = RowWeight(this)
    val set = Setting()
    setting(set)
    set.apply(layout)
    +layout

    action(layout)
    return layout
}

fun Layout.columnWeight(setting: (Setting) -> Unit = {}, action: ColumnWeight.() -> Unit): Layout {
    val layout = ColumnWeight(this)
    val set = Setting()
    setting(set)
    set.apply(layout)
    +layout

    action(layout)
    return layout
}