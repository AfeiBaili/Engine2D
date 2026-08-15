package cn.afeibaili.gl.render.layout


/**
 * # 布局组件
 *
 * @author AfeiBaili
 * @version 2026/8/13 21:15
 */

interface Component {
    var relativeX: Float
    var relativeY: Float
    val absoluteX: Float
    val absoluteY: Float
    var offsetX: Float
    var offsetY: Float
    var width: Float
    var height: Float
    var container: UnknownLayout

    fun set(x: Float, y: Float, width: Float, height: Float) {
        this.relativeX = x
        this.relativeY = y
        this.width = width
        this.height = height
    }

    fun Setting.setWidth(width: Float) = apply {
        this@Component.width = width
    }

    fun Setting.setHeight(height: Float) = apply {
        this@Component.height = height
    }

    fun Setting.setOffsetX(x: Float) = apply {
        this@Component.offsetX = x
    }

    fun Setting.setOffsetY(y: Float) = apply {
        this@Component.offsetY = y
    }

    fun Setting.setContainer(container: Layout<*>) = apply {
        this@Component.container = container
    }

    fun Component.setting(action: Component.(Setting) -> Unit) {
        action(this, Setting())
    }
}