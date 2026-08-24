package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.render.layout.align.AbstractAlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting
import cn.afeibaili.gl.render.layout.weigth.AbstractWeightLayout
import cn.afeibaili.gl.render.layout.weigth.WeightSetting

/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

open class Rectangle(val key: String, override var container: Layout) : AbstractComponent() {
    override var weight: Float = 0f
}

fun AbstractWeightLayout.rectangle(setting: (WeightSetting) -> Unit = {}, key: String): Rectangle {
    return buildRectangle(this, key, WeightSetting().also { setting(it) })
}

fun AbstractAlignmentLayout.rectangle(setting: (AlignmentSetting) -> Unit = {}, key: String): Rectangle {
    return buildRectangle(this, key, AlignmentSetting().also { setting(it) })
}

fun Layout.rectangle(setting: (Setting) -> Unit = {}, key: String): Rectangle {
    return buildRectangle(this, key, Setting().also { setting(it) })
}

fun buildRectangle(layout: Layout, key: String, setting: AbstractSetting<*>): Rectangle {
    val rectangle = Rectangle(key, layout)
    setting.apply(rectangle)
    layout.append(rectangle)
    return rectangle
}