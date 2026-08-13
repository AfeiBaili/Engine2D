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
    override val setting: Setting = SettingWeight()
    override var x: Float = 0f
    override var y: Float = 0f
    override var offsetX: Float = 0f
    override var offsetY: Float = 0f
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