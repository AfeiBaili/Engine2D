package cn.afeibaili.gl.render.layout.image

import cn.afeibaili.gl.image.atlas.Image
import cn.afeibaili.gl.render.layout.Updatable


/**
 * # 图片更新器
 *
 * @author AfeiBaili
 * @version 2026/10/4 22:44
 */

class IconUpdater : Updatable<Icon> {
    override val map = mutableMapOf<String, Icon>()

    fun put(value: Icon) {
        map.put(value.key, value)
    }

    fun update(key: String, value: Image) {
        val icon: Icon? = map[key]
        icon?.let { it.image = value }
    }

    operator fun get(key: String): Icon? {
        return map[key]
    }

    fun forEach(handler: (Icon) -> Unit) {
        map.values.forEach(handler)
    }
}