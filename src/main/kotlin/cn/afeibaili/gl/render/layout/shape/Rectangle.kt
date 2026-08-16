package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.weigth.AbstractWeightLayout


/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

class Rectangle(override var container: Layout) : AbstractComponent() {
    override var weight: Float = 0f
}

fun AbstractWeightLayout.rectangle(): Rectangle {
    val rectangle = Rectangle(this)
    +rectangle
    return rectangle
}