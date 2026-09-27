package cn.afeibaili.gl.image.atlas

import cn.afeibaili.gl.image.ImageUtil
import java.awt.image.BufferedImage

/**
 * # 图片对象类
 *
 * @author AfeiBaili
 * @version 2026/9/25 19:18
 */

class Image(val key: String, var image: BufferedImage) {
    var x: Int = 0
    var y: Int = 0
    val width: Int = image.width
    val height: Int = image.height
    val uv = FloatArray(4)

    fun extendPixel(extendPixelSize: Int) {
        image = ImageUtil.extendSide(image, extendPixelSize)
    }

    override fun toString(): String {
        return "Image(x=$x, y=$y, width=$width, height=$height, uv=${uv.contentToString()})"
    }
}