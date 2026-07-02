package cn.afeibaili.gl.image

import cn.afeibaili.gl.tool.Index
import cn.afeibaili.gl.tool.Side
import java.awt.image.BufferedImage

/**
 * # 预处理图片，可能是动态的
 *
 * 动态，静态预处理类
 */
internal class PreProcessImageSet(val name: String, val indexImageList: List<PreProcessImageInfo>)

internal class PreProcessImageInfo(val index: Index, val side: Side, var image: BufferedImage) {
    companion object {
        fun Triple<Index, BufferedImage, Side>.transform(): PreProcessImageInfo {
            return PreProcessImageInfo(first, third, second)
        }
    }
}
