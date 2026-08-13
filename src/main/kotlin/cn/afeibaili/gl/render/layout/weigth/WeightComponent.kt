package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.Component

/**
 * # 权重布局
 *
 * @author AfeiBaili
 * @version 2026/8/10 12:43
 */
interface WeightComponent : Component {
    var weight: Float

    fun setWeight(weight: Float) = apply {
        this@WeightComponent.weight = weight
    }
}