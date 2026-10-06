package cn.afeibaili.gl.render.layout.image

import cn.afeibaili.gl.image.atlas.Image


/**
 * # 图片更新器
 *
 * @author AfeiBaili
 * @version 2026/10/4 22:44
 */

class IconUpdater {
    val map = mutableMapOf<String, Icon>()

    fun put(icon: Icon) {
        map.put(icon.key, icon)
    }

    fun update(key: String, image: Image) {
        val icon: Icon? = map[key]
        icon?.let { it.image = image }
    }

    operator fun get(key: String): Icon? {
        return map[key]
    }

    fun forEach(action: (Icon) -> Unit) {
        map.values.forEach(action)
    }
}