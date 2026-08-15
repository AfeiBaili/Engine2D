package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.UnknownLayout

fun UnknownLayout.rowWeight(action: RowWeight.() -> Unit): UnknownLayout {
    val weight = RowWeight(this)
    action(weight)
    weight.initWeight()
    return weight
}

fun UnknownLayout.columnWeight(action: ColumnWeight.() -> Unit): UnknownLayout {
    val weight = ColumnWeight(this)
    action(weight)
    weight.initWeight()
    return weight
}