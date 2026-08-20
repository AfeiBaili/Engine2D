package cn.afeibaili.gl.render.layout.weigth

import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.render.layout.align.AbstractAlignmentLayout
import cn.afeibaili.gl.render.layout.align.AlignmentSetting

fun AbstractWeightLayout.rowWeight(setting: (WeightSetting) -> Unit = {}, action: RowWeight.() -> Unit): Layout {
    return buildLayoutRowWeight(this, WeightSetting().also { setting(it) }).also { action(it) }
}

fun AbstractWeightLayout.columnWeight(setting: (WeightSetting) -> Unit = {}, action: ColumnWeight.() -> Unit): Layout {
    return buildLayoutColumnWeight(this, WeightSetting().also { setting(it) }).also { action(it) }
}

fun AbstractAlignmentLayout.rowWeight(setting: (AlignmentSetting) -> Unit, action: RowWeight.() -> Unit): Layout {
    return buildLayoutRowWeight(this, AlignmentSetting().also { setting(it) }).also { action(it) }
}

fun AbstractAlignmentLayout.columnWeight(setting: (AlignmentSetting) -> Unit, action: ColumnWeight.() -> Unit): Layout {
    return buildLayoutColumnWeight(this, AlignmentSetting().also { setting(it) }).also { action(it) }
}

fun Layout.rowWeight(setting: (Setting) -> Unit, action: RowWeight.() -> Unit): Layout {
    return buildLayoutRowWeight(this, Setting().also { setting(it) }).also { action(it) }
}

fun Layout.columnWeight(setting: (Setting) -> Unit, action: ColumnWeight.() -> Unit): Layout {
    return buildLayoutColumnWeight(this, Setting().also { setting(it) }).also { action(it) }
}

fun buildLayoutRowWeight(parent: Layout, setting: AbstractSetting<*>): RowWeight {
    val layout = RowWeight(parent)
    setting.apply(layout)
    parent.append(layout)
    return layout
}

fun buildLayoutColumnWeight(parent: Layout, setting: AbstractSetting<*>): ColumnWeight {
    val layout = ColumnWeight(parent)
    setting.apply(layout)
    parent.append(layout)
    return layout
}