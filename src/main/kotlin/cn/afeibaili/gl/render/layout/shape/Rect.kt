package cn.afeibaili.gl.render.layout.shape

import cn.afeibaili.gl.render.layout.AbstractLayout
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.render.layout.WeightLayout


/**
 * # 矩形
 *
 * @author AfeiBaili
 * @version 2026/8/13 12:10
 */

class Rect(
    setting: Setting,
    override var container: Layout,
) : AbstractLayout(setting), WeightLayout {
    override var weight: Float = 0f
}