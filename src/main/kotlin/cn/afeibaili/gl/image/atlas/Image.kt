package cn.afeibaili.gl.image.atlas

import java.awt.image.BufferedImage

/**
 * # 图片对象类
 *
 * @author AfeiBaili
 * @version 2026/9/25 19:18
 */

class Image(val key: String, var bufferedImage: BufferedImage) {
    var atlasX: Int = 0
    var atlasY: Int = 0
    val width: Int get() = bufferedImage.width
    val height: Int get() = bufferedImage.height
    val uv = FloatArray(4)
}