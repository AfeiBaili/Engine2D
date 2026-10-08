package cn.afeibaili.gl.render.layout.shape.border

import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout

/**
 * # 边框组件
 *
 * @author AfeiBaili
 * @version 2026/10/8 14:25
 */

class BorderComponent(
    val borderWidth: Float,
    val color: Color,
    override var container: Layout,
) : AbstractComponent()