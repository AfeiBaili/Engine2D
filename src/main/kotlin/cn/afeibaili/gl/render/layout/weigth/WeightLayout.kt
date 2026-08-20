package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout

fun Layout.rowWeight(setting: (WeightSetting) -> Unit = {}, action: RowWeight.() -> Unit): Layout {
    val layout = RowWeight(this)
    val set = WeightSetting()
    setting(set)
    set.apply(layout)
    +layout

    action(layout)
    return layout
}

fun Layout.columnWeight(setting: (WeightSetting) -> Unit = {}, action: ColumnWeight.() -> Unit): Layout {
    val layout = ColumnWeight(this)
    val set = WeightSetting()
    setting(set)
    set.apply(layout)
    +layout

    action(layout)
    return layout
}