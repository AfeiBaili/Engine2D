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

        inline fun createDynamic(id: String, imageAction: () -> List<BufferedImage>): List<TextureModel> {
            val list = mutableListOf<TextureModel>()
            imageAction().forEachIndexed { i, it -> list.add(TextureModel("${id}_$i", it)) }
            return list
        }
    }
}