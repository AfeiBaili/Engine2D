package cn.afeibaili.gl.render.layout

import cn.afeibaili.gl.render.layout.shape.BackgroundRectangle


/**
 * # 布局抽象基类
 *
 * 直接用于着色器排版
 *
 * @author AfeiBaili
 * @version 2026/8/8 22:07
 */

abstract class AbstractLayout() : AbstractComponent(), Layout {
    override val items: MutableList<Component> = mutableListOf()
    override val backgroundRect: BackgroundRectangle =
        BackgroundRectangle(this::class.simpleName + this.toPointerString(), this, this)

    override fun update() {
        for (component in items) {
            if (component.isMaxWidth) component.width = this.width
            if (component.isMaxHeight) component.height = this.height

            if (component is Layout) component.update()
        }
    }
}