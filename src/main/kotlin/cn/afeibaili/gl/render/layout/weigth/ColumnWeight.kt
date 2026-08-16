package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout

/**
 * # 权重布局，基于列
 *
 * @author AfeiBaili
 * @version 2026/08/16 03:12
 */

class ColumnWeight(override var container: Layout) : AbstractWeightLayout() {
    override fun update() {
        var currentX = 0f
        for (element in items) {
            element.set(
                currentX, element.relativeY,
                this.width / weightCount * element.weight,
                this.height
            )
            currentX += element.width
            if (element is Layout) element.update()
        }
    }
}