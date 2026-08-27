package cn.afeibaili.gl.render.layout.adapt

import cn.afeibaili.gl.render.layout.Layout
import kotlin.math.max


/**
 * # 列自适应布局
 *
 * @author AfeiBaili
 * @version 2026/08/24 19:03
 */
class ColumnAdaptive(override var container: Layout) : AbstractAdaptiveLayout() {
    override fun update() {
        var currentX = 0f
        var maxHeight = 0f
        for (item in items) {
            if (item is Layout) item.update()
            maxHeight = max(maxHeight, item.height)
            item.relativeX = currentX + item.offsetX
            currentX += item.width
            width = currentX
        }
        height = maxHeight
    }
}