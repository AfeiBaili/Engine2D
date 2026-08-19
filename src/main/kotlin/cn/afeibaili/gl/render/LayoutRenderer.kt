package cn.afeibaili.gl.render

import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.render.layout.Layout
import java.io.Closeable
import kotlin.system.exitProcess


typealias Rect = cn.afeibaili.gl.render.layout.shape.Rectangle

/**
 * # 布局元素渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/16 13:09
 */
class LayoutRenderer(val rectRenderer: RectangleRenderer, val rootLayout: Layout) : Closeable {
    private val logger = LoggerFactory.create("LayoutRenderer")
    val rectangles = mutableMapOf<String, Rect>()

    private fun match(layout: Layout) {
        for (component in layout.items) {
            if (component is Layout) match(component)
            else when (component) {
                is Rect -> rectangles[component.key] = component
            }
        }
    }

    fun update() {
        rectangles.clear()
        match(rootLayout)

        logger.info("rectangle count is ${rectangles.size}")
        rectangles.forEach { (key, value) ->
            logger.debug("key:${value.key}, x:${value.absoluteX}, y:${value.absoluteY}, width:${value.width}, height:${value.height}")
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