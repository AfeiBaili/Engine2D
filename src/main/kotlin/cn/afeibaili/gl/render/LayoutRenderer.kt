package cn.afeibaili.gl.render

import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.render.layout.Layout
import java.io.Closeable


typealias Rect = cn.afeibaili.gl.render.layout.shape.Rectangle

/**
 * # 布局元素渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/16 13:09
 */
class LayoutRenderer(val rectRenderer: RectangleRenderer, val rootLayout: Layout, val showLayout: Boolean = true) :
    Closeable {
    private val logger = LoggerFactory.create("LayoutRenderer")
    val rectangles = mutableMapOf<String, Rect>()
    val layouts = mutableListOf<Layout>()

    private fun match(layout: Layout) {
        for (component in layout.items) {
            if (component is Layout) {
                layouts.add(component)
                match(component)
            } else when (component) {
                is Rect -> rectangles[component.key] = component
            }
        }
    }

    fun init() {
        update()
        logger.debug("layouts size: ${layouts.size}")
        layouts.forEach { logger.debug(it) }
        logger.debug("rectangles size: ${rectangles.size}")
        rectangles.forEach { (_, value) -> logger.debug(value) }
    }

    fun update() {
        rectangles.clear()
        layouts.clear()
        match(rootLayout)

        if (showLayout) {
            layouts.forEachIndexed { index, value ->
                rectRenderer.put(
                    index.toString(),
                    value.absoluteX,
                    value.absoluteY,
                    value.width,
                    value.height,
                    value.backgroundColor
                )
            }
        }
        rectangles.forEach { (key, value) ->
            rectRenderer.put(key, value.absoluteX, value.absoluteY, value.width, value.height, value.backgroundColor)
        }
    }

    fun render() {
        rectRenderer.render()
    }

    override fun close() {
        rectangles.clear()
        rectRenderer.close()
    }
}