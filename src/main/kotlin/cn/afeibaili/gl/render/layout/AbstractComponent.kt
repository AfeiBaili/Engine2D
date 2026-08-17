package cn.afeibaili.gl.render.layout

import cn.afeibaili.gl.render.layout.align.AlignmentType


/**
 * # 组件抽象基类
 *
 * @author AfeiBaili
 * @version 2026/8/13 21:53
 */

abstract class AbstractComponent : Component {
    override var relativeX: Float = 0f
    override var relativeY: Float = 0f
    override val absoluteY: Float
        get() = relativeY + offsetY + container.absoluteY
    override val absoluteX: Float
        get() = relativeX + offsetX + container.absoluteX
    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
    override var width: Float = 0f
    override var height: Float = 0f
    override var weight: Float = 1f
    override var align: AlignmentType = AlignmentType.CENTER

    override fun toString(): String {
        return "x: $absoluteX, y: $absoluteY, width: $width, height: $height, weight: $weight"
    }
}