package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Component


/**
 * # 权重设置器
 *
 * @author AfeiBaili
 * @version 2026/8/20 12:19
 */

class WeightSetting : AbstractSetting<WeightSetting>() {
    private var weight = 1f

    override fun apply(component: Component) {
        super.apply(component)
        component.weight = weight

    }

    fun weight(weight: Float) = apply {
        this.weight = weight
    }
}