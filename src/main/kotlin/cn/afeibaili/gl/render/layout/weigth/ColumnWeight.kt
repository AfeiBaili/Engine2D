package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.UnknownLayout

/**
 * # 权重布局，基于列
 *
 * @author AfeiBaili
 * @version 2026/08/16 03:12
 */

class ColumnWeight(override var container: UnknownLayout) : AbstractWeightLayout() {
    override fun initWeight() {
        var currentX = 0f
        for (element in items) {
            element.set(
                currentX, element.relativeY,
                container.width / weightCount * element.weight,
                container.height
            )
            currentX += element.width
        }
    }
}