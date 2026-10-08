package cn.afeibaili.gl.render.layout.shape.border

import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting

fun Layout.border(borderWidth: Float, color: Color, setting: (Setting) -> Unit = {}): BorderComponent {
    return builderBorder(borderWidth, color, this, Setting().also { setting(it) })
}

fun builderBorder(
    borderWidth: Float,
    color: Color,
    layout: Layout,
    setting: AbstractSetting<*>,
): BorderComponent {
    val component = BorderComponent(borderWidth, color, layout)
    setting.apply(component)
    layout.append(component)
    return component
}