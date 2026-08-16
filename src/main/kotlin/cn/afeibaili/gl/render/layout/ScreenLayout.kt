package cn.afeibaili.gl.render.layout


/**
 * # 全屏布局，铺满全屏
 *
 * @author AfeiBaili
 * @version 2026/8/11 11:51
 */

class ScreenLayout(
    override var width: Float,
    override var height: Float,
) : Layout {
    override var relativeX: Float = 0f
    override var relativeY: Float = 0f
    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
    override val absoluteX: Float = 0f
    override val absoluteY: Float = 0f
    override var weight: Float = 0f
    override val items: MutableList<Component> = mutableListOf()


    override var container: Layout
        get() = this
        set(_) {}

    override fun update() {
        items.forEach { if (it is Layout) it.update() else return@forEach }
    }

    fun update(width: Float, height: Float, x: Float = 0f, y: Float = 0f) {
        this.width = width
        this.height = height
        this.relativeX = x
        this.relativeY = y
        update()
    }

    fun layout(action: Layout.() -> Unit) = apply {
        action()
        update()
    }
}