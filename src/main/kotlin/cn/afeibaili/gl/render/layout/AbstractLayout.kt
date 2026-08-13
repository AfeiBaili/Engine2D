package cn.afeibaili.gl.render.layout


/**
 * # 布局抽象基类
 *
 * 直接用于着色器排版
 *
 * @author AfeiBaili
 * @version 2026/8/8 22:07
 */

abstract class AbstractLayout : Layout {
    override var x: Float = 0f
    override var y: Float = 0f
    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
    override var width: Float = 0f
    override var height: Float = 0f
}