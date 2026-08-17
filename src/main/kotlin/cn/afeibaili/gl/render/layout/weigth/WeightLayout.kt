package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting

fun Layout.rowWeight(setting: Setting, action: RowWeight.() -> Unit): Layout {
    val weight = RowWeight(this)
    setting.apply(weight)
    +weight

    action(weight)
    return weight
}

fun Layout.columnWeight(setting: Setting, action: ColumnWeight.() -> Unit): Layout {
    val weight = ColumnWeight(this)
    setting.apply(weight)
    +weight

    action(weight)
    return weight
}