package cn.afeibaili.gl.image

import cn.afeibaili.gl.tool.Index
import cn.afeibaili.gl.tool.Side
import cn.afeibaili.gl.tool.Size
import java.awt.image.BufferedImage

/**
 * # 图集
 *
 * 包含所有纹理信息的纹理图集，其中纹理的大小都是相同的。
 *
 * @param atlasId 图集id用来区分图集，使用索引规则
 * @param bufferedImage 图片信息
 * @param textureNameMap 纹理id和索引信息，可能有多个索引（动态纹理）
 * @param textureSize 此图集包含的纹理数量
 * @param textureSide 纹理边长
 * @param atlasSide 此图集的边长
 * @param texture 纹理类，由OpenGL管理
 * @param rowLength 行纹理数量
 *
 * @author AfeiBaili
 */

class Atlas(
    val atlasId: Index,                             // 图集id
    val bufferedImage: BufferedImage,               // 图集缓存
    val textureNameMap: Map<String, List<Index>>,   // 纹理名称映射
    val textureSize: Size,                          // 纹理数量
    val textureSide: Side,                          // 纹理边长
    val atlasSide: Side,                            // 图集边长
    val texture: Texture,                           // 纹理实例
    val rowLength: Int,                             // 行数量
)