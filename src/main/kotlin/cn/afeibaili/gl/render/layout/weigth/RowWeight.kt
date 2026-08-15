package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.UnknownLayout

/**
 * # 权重布局，基于行
 *
 * @author AfeiBaili
 * @version 2026/08/16 03:13
 */

class RowWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
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
