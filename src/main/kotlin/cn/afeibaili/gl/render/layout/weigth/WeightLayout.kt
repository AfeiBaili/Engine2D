package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.UnknownLayout

class RowWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
        val weightValue = container.height / weightCount
        var currentY = 0f
        for (element in items) {
            element.height = weightValue * element.weight
            element.width = container.width
            element.y = currentY
            currentY += element.height
        }
    }
}

class ColumnWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
        val weightValue = container.width / weightCount
        var currentX = 0f
        for (element in items) {
            element.width = weightValue * element.weight
            element.height = container.height
            element.x = currentX
            currentX += element.width
        }
    }
}

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