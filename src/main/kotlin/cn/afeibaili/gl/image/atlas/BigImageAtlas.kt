package cn.afeibaili.gl.image.atlas

import cn.afeibaili.gl.image.Texture


/**
 * # 图片集
 *
 * @author AfeiBaili
 * @version 2026/10/4 00:00
 */

interface BigImageAtlas {
    val images: List<Image>
    val imageMap: Map<String, Image>
    fun apply()
    fun toTexture(): Texture
    fun generateUv()
    operator fun get(key: String): Image =
        imageMap[key] ?: throw IllegalArgumentException("未知的图片Key: $key")
}