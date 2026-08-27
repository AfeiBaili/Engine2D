package cn.afeibaili.gl.render

import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.shape.Rectangle
import cn.afeibaili.gl.render.layout.text.Text
import cn.afeibaili.gl.render.layout.text.TextUpdater
import java.io.Closeable


/**
 * # 布局元素渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/16 13:09
 */
class LayoutRenderer(
    val textRenderer: TextLayoutRenderer,
    val rectRenderer: RectangleRenderer,
    val rootLayout: Layout,
) :
    Closeable {
    private val logger = LoggerFactory.create("LayoutRenderer")

    val rectangles = mutableMapOf<String, Rectangle>()
    val layouts = mutableListOf<Layout>()
    var updaters = mutableSetOf<TextUpdater>()

    private fun match(layout: Layout) {
        if (!layout.showable) return
        for (component in layout.items) {
            if (component is Layout) {
                layouts.add(component)
                match(component)
            } else when (component) {
                is Rectangle -> rectangles[component.key] = component
                is Text -> updaters.add(component.updater)
            }
        }
    }

    fun init() {
        match(rootLayout)
        update()
        logger.debug("layouts size: ${layouts.size}")
        layouts.forEach { logger.debug(it) }
        logger.debug("rectangles size: ${rectangles.size}")
        rectangles.forEach { (_, value) -> logger.debug(value) }
        logger.debug("text size: ${updaters.sumOf { it.map.size }}")
        updaters.forEach { it.map.values.forEach { it -> logger.debug(it) } }
    }

    fun update() {
        textRenderer.clear()
        rectRenderer.clear()
        rectangles.clear()
        updaters.clear()
        layouts.clear()
        match(rootLayout)

        updaters.forEach { textRenderer.upload(it) }
        layouts.forEach { rectRenderer.put(it.backgroundRect) }
        rectangles.forEach { (_, value) -> rectRenderer.put(value) }
        updaters.forEach { it.forEach { it -> rectRenderer.put(it.backgroundRect) } }
    }

    fun render() {
        rectRenderer.render()
        textRenderer.render()
    }

    override fun close() {
        rectangles.clear()
        layouts.clear()
        updaters.clear()
        rectRenderer.close()
        textRenderer.close()
    }
}