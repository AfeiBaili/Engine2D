package cn.afeibaili.gl.render.layout


/**
 * # 布局抽象基类
 *
 * 直接用于着色器排版
 *
 * @author AfeiBaili
 * @version 2026/8/8 22:07
 */

abstract class AbstractLayout<Component : ComponentType>() : AbstractComponent(), Layout<Component> {
    override val items: MutableList<Component> = mutableListOf()
}