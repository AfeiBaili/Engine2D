package cn.afeibaili.gl.render.layout

/**
 * # 布局属性设置类
 *
 * @author AfeiBaili
 * @version 2026/8/9 16:24
 */
class Setting {
    var width = 0f
    var height = 0f
    var offsetX = 0f
    var offsetY = 0f
    var weight = 1f

    internal fun apply(component: Component) {
        component.width = width
        component.height = height
        component.offsetX = offsetX
        component.offsetY = offsetY
        component.weight = weight
    }

    fun setWidth(width: Float) = apply {
        this.width = width
    }

    fun setHeight(height: Float) = apply {
        this.height = height
    }

    fun setOffsetX(x: Float) = apply {
        this.offsetX = x
    }

    fun setOffsetY(y: Float) = apply {
        this.offsetY = y
    }

    fun setSize(width: Float, height: Float) = apply {
        this.width = width
        this.height = height
    }

    fun setSize(parent: Layout) = apply {
        this.width = parent.width
        this.height = parent.height
    }

    fun setWeight(weight: Float) = apply {
        this.weight = weight
    }
}