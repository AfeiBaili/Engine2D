package cn.afeibaili.gl.image

import java.awt.image.BufferedImage


/**
 * # 纹理模型
 *
 * @author AfeiBaili
 * @version 2026/6/29 21:36
 */

class TextureModel(val id: String, val image: BufferedImage) {
    companion object {
        inline fun create(id: String, imageAction: () -> BufferedImage): TextureModel {
            return TextureModel(id, imageAction())
        }
    }
}