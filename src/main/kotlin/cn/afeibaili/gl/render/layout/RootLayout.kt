package cn.afeibaili.gl.render.layout


/**
 * # 全屏布局，铺满全屏
 *
 * @author AfeiBaili
 * @version 2026/8/11 11:51
 */

class RootLayout(
    override var width: Float,
    override var height: Float,
) : AbstractLayout() {
    override val absoluteY: Float = 0f
    override val absoluteX: Float = 0f
    override var container: Layout
        get() = this
        set(_) {}

    fun update(width: Float, height: Float) {
        this.width = width
        this.height = height
        update()
    }

    fun layout(action: Layout.() -> Unit) = apply {
        action()
        update()
    }
}