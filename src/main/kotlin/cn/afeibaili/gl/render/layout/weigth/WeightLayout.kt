package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout

fun Layout.rowWeight(action: RowWeight.() -> Unit): Layout {
    val weight = RowWeight(this)
    +weight

    action(weight)
    weight.update()
    return weight
}

fun Layout.columnWeight(action: ColumnWeight.() -> Unit): Layout {
    val weight = ColumnWeight(this)
    +weight

    action(weight)
    weight.update()
    return weight
}