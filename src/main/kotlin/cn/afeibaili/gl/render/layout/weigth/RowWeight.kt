package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Layout

/**
 * # 权重布局，基于行
 *
 * @author AfeiBaili
 * @version 2026/08/16 03:13
 */

class RowWeight(override var container: Layout) : AbstractWeightLayout() {
    override fun update() {
        var currentY = 0f
        for (element in items) {
            element.set(
                element.relativeX, currentY,
                container.width,
                container.height / weightCount * element.weight
            )
            currentY += element.height
        }
    }
}
