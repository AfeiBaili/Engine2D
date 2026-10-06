package cn.afeibaili.gl.render.layout.image

import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.layout.AbstractSetting
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting


/**
 * # 图标组件
 *
 * @author AfeiBaili
 * @version 2026/9/3 18:50
 */

class Icon(override var container: Layout, key: String, var image: Image, iconUpdater: IconUpdater) :
    AbstractImageComponent(key, iconUpdater)

// LAYOUT /////////////
fun Layout.icon(key: String, image: Image, updater: IconUpdater, setting: (Setting) -> Unit = {}): Icon {
    return buildStaticIcon(key, image, updater, this, Setting().also { setting(it) }).apply { updater.put(this) }
}

fun buildStaticIcon(
    key: String,
    image: Image,
    iconUpdater: IconUpdater,
    layout: Layout,
    setting: AbstractSetting<*>,
): Icon {
    val staticIcon = Icon(layout, key, image, iconUpdater)
    setting.apply(staticIcon)
    layout.append(staticIcon)
    return staticIcon
}