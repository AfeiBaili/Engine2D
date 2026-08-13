package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.UnknownLayout
import cn.afeibaili.gl.render.layout.weigth.AbstractWeightLayout
import cn.afeibaili.gl.render.layout.weigth.WeightComponent


/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

class Rectangle(override var container: UnknownLayout) : AbstractComponent(), WeightComponent {
    override var weight: Float = 0f
}

fun AbstractWeightLayout.rectangle(): Rectangle {
    val rectangle = Rectangle(this)
    +rectangle
    return rectangle
}