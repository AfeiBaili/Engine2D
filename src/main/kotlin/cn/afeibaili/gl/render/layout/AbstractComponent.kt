package cn.afeibaili.gl.render.layout


/**
 * # 组件抽象基类
 *
 * @author AfeiBaili
 * @version 2026/8/13 21:53
 */

abstract class AbstractComponent : Component {
    override var x: Float = 0f
        get() = container.x + offsetX + field
    override var y: Float = 0f
        get() = container.y + offsetY + field

    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
    override var width: Float = 0f
    override var height: Float = 0f
}