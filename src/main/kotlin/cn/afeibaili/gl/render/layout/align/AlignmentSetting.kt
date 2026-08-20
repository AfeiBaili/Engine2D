package cn.afeibaili.gl.render.layout.align

import cn.afeibaili.gl.render.layout.Component
import cn.afeibaili.gl.render.layout.AbstractSetting


/**
 * # 排列设置器
 *
 * @author AfeiBaili
 * @version 2026/8/20 12:23
 */

class AlignmentSetting : AbstractSetting<AlignmentSetting>() {
    private var align: AlignmentType = AlignmentType.CENTER

    override fun apply(component: Component) {
        super.apply(component)
        component.align = align
    }

    fun align(type: AlignmentType) = apply {
        this.align = type
    }
}