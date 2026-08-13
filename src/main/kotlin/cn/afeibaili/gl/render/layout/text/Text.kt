package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.render.layout.AbstractLayout
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.WeightLayout

/**
 * # 文本布局类
 *
 * @author AfeiBaili
 * @version 2026/08/11 11:50
 */

class Text(
    val key: String,
    var text: String, updater: TextUpdater? = null,
    override var container: Layout,
) : AbstractLayout(), WeightLayout {
    override var weight: Float = 0f

    init {
        updater?.put(key, this)
    }
}