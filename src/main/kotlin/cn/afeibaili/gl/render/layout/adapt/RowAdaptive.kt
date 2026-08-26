package cn.afeibaili.gl.render.layout.adapt

import cn.afeibaili.gl.render.layout.Layout
import kotlin.math.max

/**
 * # 行自适应布局
 *
 * @author AfeiBaili
 * @version 2026/08/24 19:03
 */
class RowAdaptive(override var container: Layout) : AbstractAdaptiveLayout() {
    override fun update() {
        var currentY = 0f
        var maxWidth = 0f
        for (item in items) {
            if (item is Layout) item.update()
            maxWidth = max(maxWidth, item.width)
            item.relativeY += currentY
            currentY += item.height
            height = currentY
        }
        width = maxWidth
    }
}