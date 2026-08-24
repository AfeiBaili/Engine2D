package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.Color
import cn.afeibaili.gl.render.layout.Component
import cn.afeibaili.gl.render.layout.Layout


/**
 * # 布局用矩形
 *
 * @author AfeiBaili
 * @version 2026/8/24 19:11
 */

class BackgroundRectangle(key: String, val component: Component, layout: Layout) : Rectangle(key, layout) {
    override val absoluteX: Float
        get() = component.absoluteX
    override val absoluteY: Float
        get() = component.absoluteY
    override var width: Float
        get() = component.width
        set(value) {
            component.width = value
        }
    override var height: Float
        get() = component.height
        set(value) {
            component.height = value
        }
    override var backgroundColor: Color
        get() = component.backgroundColor
        set(value) {
            super.backgroundColor = value
        }
}