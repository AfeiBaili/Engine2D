package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.UnknownLayout
import cn.afeibaili.gl.render.layout.weigth.WeightComponent

/**
 * # 文本布局类
 *
 * @author AfeiBaili
 * @version 2026/08/11 11:50
 */

class Text(
    val key: String,
    var text: String, updater: TextUpdater? = null,
    override var container: UnknownLayout,
) : AbstractComponent(), WeightComponent {
    override var weight: Float = 0f

    init {
        updater?.put(key, this)
    }
}