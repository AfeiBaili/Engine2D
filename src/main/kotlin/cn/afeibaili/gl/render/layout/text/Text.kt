package cn.afeibaili.gl.render.layout.text

import cn.afeibaili.gl.render.layout.AbstractComponent
import cn.afeibaili.gl.render.layout.Layout

/**
 * # 文本布局类
 *
 * @author AfeiBaili
 * @version 2026/08/11 11:50
 */

class Text(
    val key: String,
    val size: Float,
    var text: String, updater: TextUpdater? = null,
    override var container: Layout,
) : AbstractComponent() {
    override var weight: Float = 0f

    init {
        updater?.put(key, this)
    }
}