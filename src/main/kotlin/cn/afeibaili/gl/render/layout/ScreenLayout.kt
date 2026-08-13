package cn.afeibaili.gl.render.layout


/**
 * # 全屏布局，铺满全屏
 *
 * @author AfeiBaili
 * @version 2026/8/11 11:51
 */

class ScreenLayout : AbstractLayout() {
    override var container: Layout
        get() = this
        set(_) {}

    fun update(width: Float, height: Float, x: Float = 0f, y: Float = 0f) {
        this.width = width
        this.height = height
        this.x = x
        this.y = y
    }
}