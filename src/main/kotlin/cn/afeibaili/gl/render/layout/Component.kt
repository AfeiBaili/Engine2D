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
    var container: Layout
    var weight: Float

    fun set(x: Float, y: Float, width: Float, height: Float) {
        this.relativeX = x
        this.relativeY = y
        this.width = width
        this.height = height
    }
}