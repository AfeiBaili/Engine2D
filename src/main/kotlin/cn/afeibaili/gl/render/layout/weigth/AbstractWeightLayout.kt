package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.AbstractLayout


/**
 * # 抽象的权重布局器
 *
 * @author AfeiBaili
 * @version 2026/8/13 21:20
 */

abstract class AbstractWeightLayout : AbstractLayout<WeightComponent>() {
    val weightCount: Float
        get() {
            var value = 0f
            for (element in items) value += element.weight
            return value
        }

    abstract fun initWeight()
}