package cn.afeibaili.gl.render.layout


/**
 * #  布局接口
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:51
 */


interface Layout<Component : ComponentType> : ComponentType {
    val items: MutableList<Component>

    fun append(layout: Component): Layout<Component> {
        items.add(layout)
        return this
    }

    operator fun Component.unaryPlus() {
        append(this)
    }
}