package cn.afeibaili.gl.render.layout.image

import cn.afeibaili.gl.render.layout.AbstractComponent
import java.awt.image.BufferedImage


/**
 * # 图片信息
 *
 * @author AfeiBaili
 * @version 2026/9/3 18:58
 */

abstract class AbstractImageComponent(val key: String, val bufferedImage: BufferedImage) : AbstractComponent() {
    val uv = FloatArray(4)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractImageComponent) return false

        if (key != other.key) return false
        if (bufferedImage != other.bufferedImage) return false
        if (!uv.contentEquals(other.uv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + bufferedImage.hashCode()
        result = 31 * result + uv.contentHashCode()
        return result
    }
}