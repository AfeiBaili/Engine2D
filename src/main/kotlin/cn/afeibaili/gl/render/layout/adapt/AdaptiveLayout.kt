package cn.afeibaili.gl.render.layout.adapt

import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.render.layout.align.AlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting


fun AlignmentLayout.rowAdapt(setting: (AlignmentSetting) -> Unit = {}, action: RowAdaptive.() -> Unit): RowAdaptive {
    return buildRowAdapt(this, AlignmentSetting().also { setting(it) }).also { action(it) }
}

fun AlignmentLayout.columnAdapt(setting: (AlignmentSetting) -> Unit = {}, action: ColumnAdaptive.() -> Unit): ColumnAdaptive {
    return buildColumnAdapt(this, AlignmentSetting().also { setting(it) }).also { action(it) }
}

fun Layout.rowAdapt(setting: (Setting) -> Unit = {}, action: RowAdaptive.() -> Unit): RowAdaptive {
    return buildRowAdapt(this, Setting().also { setting(it) }).also { action(it) }
}

fun Layout.columnAdapt(setting: (Setting) -> Unit = {}, action: ColumnAdaptive.() -> Unit): ColumnAdaptive {
    return buildColumnAdapt(this, Setting().also { setting(it) }).also { action(it) }
}

private fun buildRowAdapt(parent: Layout, setting: AbstractSetting<*>): RowAdaptive {
    val layout = RowAdaptive(parent)
    setting.apply(layout)
    parent.append(layout)
    return layout
}

private fun buildColumnAdapt(parent: Layout, setting: AbstractSetting<*>): ColumnAdaptive {
    val layout = ColumnAdaptive(parent)
    setting.apply(layout)
    parent.append(layout)
    return layout
}