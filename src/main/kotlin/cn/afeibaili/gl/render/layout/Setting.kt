package cn.afeibaili.gl.render.layout


/**
 * # 布局属性设置类
 *
 * @author AfeiBaili
 * @version 2026/8/9 16:24
 */
open class Setting {
    fun Layout.setX(x: Float) = apply {
        this.x = x
    }

    fun Layout.setY(y: Float) = apply {
        this.y = y
    }

    fun Layout.setWidth(width: Float) = apply {
        this.width = width
    }

    fun Layout.setHeight(height: Float) = apply {
        this.height = height
    }

    fun Layout.setOffsetX(x: Float) = apply {
        this.offsetX = x
    }

    fun Layout.setOffsetY(y: Float) = apply {
        this.offsetY = y
    }

    fun Layout.setContainer(container: Layout) = apply {
        this.container = container
    }
}

/**
 * # 权重设置器
 *
 * @author AfeiBaili
 * @version 2026/08/10 12:53
 */
class SettingWeight : Setting() {
    fun WeightLayout.setWeight(weight: Float) = apply {
        this.weight = weight
    }
}