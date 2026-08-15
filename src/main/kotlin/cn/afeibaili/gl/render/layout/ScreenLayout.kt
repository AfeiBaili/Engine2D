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
) : Layout<UnknownLayout> {
    override var relativeX: Float = 0f
    override var relativeY: Float = 0f
    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
    override val absoluteX: Float = 0f
    override val absoluteY: Float = 0f
    override val items: MutableList<UnknownLayout> = mutableListOf()


    override var container: UnknownLayout
        get() = this
        set(_) {}

    fun update(width: Float, height: Float, x: Float = 0f, y: Float = 0f) {
        this.width = width
        this.height = height
        this.relativeX = x
        this.relativeY = y
    }
}