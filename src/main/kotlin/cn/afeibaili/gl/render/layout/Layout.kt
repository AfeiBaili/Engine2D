package cn.afeibaili.gl.render.layout

import cn.afeibaili.gl.render.layout.shape.BackgroundRectangle


/**
 * #  布局接口
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:51
 */


interface Layout : Component {
    val items: MutableList<Component>
    val backgroundRect: BackgroundRectangle

    fun append(layout: Component): Layout {
        items.add(layout)
        return this
    }

    operator fun Component.unaryPlus() {
        append(this)
    }

    fun update()
}