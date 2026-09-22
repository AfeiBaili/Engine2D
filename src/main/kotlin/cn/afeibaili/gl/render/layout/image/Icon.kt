package cn.afeibaili.gl.render.layout.image

import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.util.randomUuid
import java.awt.image.BufferedImage


/**
 * # 图标组件
 *
 * @author AfeiBaili
 * @version 2026/9/3 18:50
 */

class Icon(override var container: Layout, bufferedImage: BufferedImage) :
    AbstractImageComponent(randomUuid(), bufferedImage)

fun Layout.icon(image: BufferedImage, setting: Setting.() -> Unit): Icon {
    return buildIcon(image, this, Setting().also { setting(it) })
}

fun buildIcon(image: BufferedImage, layout: Layout, setting: AbstractSetting<*>): Icon {
    val icon = Icon(layout, image)
    setting.apply(icon)
    layout.append(icon)
    return icon
}