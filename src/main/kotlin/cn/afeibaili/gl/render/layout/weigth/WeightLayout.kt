package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.UnknownLayout

class RowWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
        var currentY = 0f
        for (element in items) {
            element.set(
                element.x, currentY,
                container.width,
                container.height / weightCount * element.weight
            )
            currentY += element.height
        }
    }
}

class ColumnWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
        var currentX = 0f
        for (element in items) {
            element.set(
                currentX, element.y,
                container.width / weightCount * element.weight,
                container.height
            )
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