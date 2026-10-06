package cn.afeibaili.gl.image.atlas

import cn.afeibaili.gl.util.Time


/**
 * # 动态图片
 *
 * 包含多个静态图片的动态图片
 *
 * @author AfeiBaili
 * @version 2026/10/4 00:28
 */

class DynamicImage(val key: String, val switchMillisInternal: Int, var images: Array<Image>) {
    var indexImage = 0
    var lastMilli: Long = Time.millis()
    var changed = false
    var accumulator = 0L

    fun getImage(): Image {
        changed = false
        return images[indexImage]
    }

    fun getNextImage(): Image {
        if (++indexImage >= images.size) {
            indexImage = 0
        }
        return getImage()
    }

    fun update() {
        val currentMillis = Time.millis()
        val delta = currentMillis - lastMilli
        lastMilli = currentMillis
        accumulator += delta
        while (accumulator > switchMillisInternal) {
            accumulator -= switchMillisInternal
            getNextImage()
            changed = true
        }
    }
}