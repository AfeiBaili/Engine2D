package cn.afeibaili.gl.render.layout

import cn.afeibaili.gl.render.Color

/**
 * # 布局属性设置类
 *
 * @author AfeiBaili
 * @version 2026/8/9 16:24
 */
abstract class AbstractSetting<Setting : AbstractSetting<Setting>> {
    private var width = 0f
    private var height = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var backgroundColor = Color.NONE
    private var isMaxWidth = false
    private var isMaxHeight = false

    internal open fun apply(component: Component) {
        component.width = width
        component.height = height
        component.offsetX = offsetX
        component.offsetY = offsetY
        component.backgroundColor = backgroundColor
        component.isMaxWidth = isMaxWidth
        component.isMaxHeight = isMaxHeight
    }

    @Suppress("UNCHECKED_CAST")
    fun case(): Setting {
        return this as Setting
    }

    fun width(width: Float) = case().apply {
        this.width = width
    }

    fun height(height: Float) = case().apply {
        this.height = height
    }

    fun offsetX(x: Float) = case().apply {
        this.offsetX = x
    }

    fun offsetY(y: Float) = case().apply {
        this.offsetY = y
    }

    fun size(width: Float, height: Float) = case().apply {
        this.width = width
        this.height = height
    }

    fun backgroundColor(color: Color) = case().apply {
        this.backgroundColor = color
    }

    fun maxSize() = case().apply {
        maxWidth()
        maxHeight()
    }

    fun maxWidth() = case().apply {
        this.isMaxWidth = true
    }

    fun maxHeight() = case().apply {
        this.isMaxHeight = true
    }
}