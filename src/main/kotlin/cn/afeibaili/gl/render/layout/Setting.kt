package cn.afeibaili.gl.render.layout

import cn.afeibaili.gl.render.layout.align.AlignmentType

/**
 * # 布局属性设置类
 *
 * @author AfeiBaili
 * @version 2026/8/9 16:24
 */
open class Setting {
    var width = 0f
    var height = 0f
    var offsetX = 0f
    var offsetY = 0f
    var weight = 1f
    var align: AlignmentType = AlignmentType.CENTER


    internal open fun apply(component: Component) {
        component.width = width
        component.height = height
        component.offsetX = offsetX
        component.offsetY = offsetY
        component.weight = weight
        component.align = align
    }

    fun width(width: Float) = apply {
        this.width = width
    }

    fun height(height: Float) = apply {
        this.height = height
    }

    fun offsetX(x: Float) = apply {
        this.offsetX = x
    }

    fun offsetY(y: Float) = apply {
        this.offsetY = y
    }

    fun align(type: AlignmentType) {
        this.align = type
    }

    fun weight(weight: Float) {
        this.weight = weight
    }

    fun size(width: Float, height: Float) = apply {
        this.width = width
        this.height = height
    }

    fun size(parent: Layout) = apply {
        this.width = parent.width
        this.height = parent.height
    }
}