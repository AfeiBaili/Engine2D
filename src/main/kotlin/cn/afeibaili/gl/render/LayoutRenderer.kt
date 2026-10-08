package cn.afeibaili.gl.render

import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.image.Icon
import cn.afeibaili.gl.render.layout.image.IconUpdater
import cn.afeibaili.gl.render.layout.shape.Rectangle
import cn.afeibaili.gl.render.layout.shape.border.BorderComponent
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
    val imageRenderer: ImageCollectionRenderer,
    val borderRenderer: BorderRenderer,
    val rootLayout: Layout,
) :
    Closeable {
    private val logger = LoggerFactory.create("LayoutRenderer")

    val rectangles = mutableMapOf<String, Rectangle>()
    val layouts = mutableListOf<Layout>()
    var texts = mutableSetOf<TextUpdater>()
    val images = mutableSetOf<IconUpdater>()
    val borders = mutableSetOf<BorderComponent>()

    private fun match(layout: Layout) {
        if (!layout.showable) return
        for (component in layout.items) {
            if (component is Layout) {
                layouts.add(component)
                match(component)
            } else when (component) {
                is Rectangle -> rectangles[component.key] = component
                is Text -> texts.add(component.updater)
                is Icon -> images.add(component.updater)
                is BorderComponent -> borders.add(component)
            }
        }
    }

    fun init() {
        match(rootLayout)
        update()
        imageRenderer.apply()
        logger.debug("borders size: ${borders.size}")
        logger.debug("image updaters size: ${images.size}")
        logger.debug("layouts size: ${layouts.size}")
        layouts.forEach { logger.debug(it) }
        logger.debug("rectangles size: ${rectangles.size}")
        rectangles.forEach { (_, value) -> logger.debug(value) }
        logger.debug("text size: ${texts.sumOf { it.map.size }}")
        texts.forEach { it.map.values.forEach { it -> logger.debug(it) } }
    }

    fun update() {
        textRenderer.clear()
        rectRenderer.clear()
        rectangles.clear()
        borderRenderer.clear()
        texts.clear()
        images.clear()
        layouts.clear()
        borders.clear()
        match(rootLayout)

        borders.forEach { borderRenderer.add(it) }
        images.forEach { imageRenderer.add(it) }
        texts.forEach { textRenderer.add(it) }
        layouts.forEach { rectRenderer.put(it.backgroundRect) }
        rectangles.forEach { (_, value) -> rectRenderer.put(value) }
        texts.forEach { it.forEach { it -> rectRenderer.put(it.backgroundRect) } }
    }

    fun render() {
        rectRenderer.render()
        textRenderer.render()
        imageRenderer.render()
        borderRenderer.render()
    }

    override fun close() {
        rectangles.clear()
        layouts.clear()
        texts.clear()
        images.clear()
        rectRenderer.close()
        textRenderer.close()
        imageRenderer.close()
    }
}