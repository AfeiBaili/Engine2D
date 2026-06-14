package cn.afeibaili.gl.image

import cn.afeibaili.gl.exception.ArrayException
import cn.afeibaili.gl.exception.ImageException
import cn.afeibaili.gl.logger.LoggerFactory
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.sqrt


/**
 * # 纹理图集
 *
 * @author AfeiBaili
 * @version 2026/6/4 20:55
 */

class TextureAtlas(val atlas: List<Atlas>, val extendPixel: Int) {

    /**
     * 获取纹理UV坐标
     *
     * @param id 图片id
     * @param uv 数组，长度需为4
     */
    fun getUv(id: String, outUv: FloatArray) {
        //todo 修正如何获取uv
        if (outUv.size != 4) throw ArrayException("uv数组大小不为4")
        val atlas: Atlas = getAtlas(id) ?: throw ImageException("图集为空，id不正确: $id")
        val (x, y) = atlas.nameMap[id]!!

        val atlasSide = atlas.atlasSide.value.toFloat()
        val imageSide = atlas.imageSide.value.toFloat()

        val texelClamp = 0.5f

        outUv[0] = (x.toFloat() + texelClamp) / atlasSide
        outUv[1] = (atlasSide - (y.toFloat() + imageSide) + texelClamp) / atlasSide
        outUv[2] = (x.toFloat() + imageSide - texelClamp) / atlasSide
        outUv[3] = (atlasSide - y.toFloat() - texelClamp) / atlasSide
    }

    fun getAtlas(id: String): Atlas? = atlas.find { it.nameMap[id] != null }

    companion object {
        private val logger = LoggerFactory.create("TextureAtlas")

        /**
         * 创建纹理图集 (不过滤图片)
         *
         * 将文件列表的文件转换为一个大图
         *
         * @param name 文件名（id）
         * @param imageFiles 文件列表
         * @param extendPixel 防止纹理“流血”的扩展像素，默认为1
         */
        fun create(name: String, imageFiles: List<File>, extendPixel: Int = 1): TextureAtlas {
            //// 根据图片大小区分图集 ///////////////////////////
            val atlasMap = HashMap<Side, MutableList<PreProcessImage>>()  //根据图片大小分类图片
            imageFiles.forEach { file ->
                val name = runCatching {
                    file.name.split(".").first()
                }.getOrElse {
                    throw ImageException("无法获取文件名字: ${file.canonicalPath}。请用png格式")
                }
                val readImageBuffer: BufferedImage = runCatching {
                    ImageIO.read(file)
                }.getOrElse { throw ImageException("此文件无法转为图片: ${file.canonicalPath}") }
                val width: Int = readImageBuffer.width
                val height: Int = readImageBuffer.height
                if (width != height) throw ImageException("图片宽高不一致，请用正方形图片")
                val side = Side(width)
                val images = atlasMap[side]

                if (images != null) images.add(PreProcessImage(name, readImageBuffer))
                else atlasMap[side] = mutableListOf(PreProcessImage(name, readImageBuffer))
            }
            //// 扩展图片 /////////////////////////////////////
            atlasMap.forEach { (side, images) ->
                images.forEach { image ->
                    image.bufferedImage = extendSide(image.bufferedImage, extendPixel)
                }
            }

            //// 转换图集 /////////////////////////////////////
            val atlases = mutableListOf<Atlas>()
            atlasMap.forEach { (side, images) ->
                val ceil = ceil(sqrt(images.size.toDouble())).toInt()
                val atlasSide: Int = ceil * side.value + ceil * (extendPixel shl 1)
                val atlasBufferImage = BufferedImage(atlasSide, atlasSide, BufferedImage.TYPE_INT_ARGB)
                val nameMap = HashMap<String, Pair<Int, Int>>()

                var currentX: Int
                var currentY: Int
                images.forEachIndexed { index, ppi ->
                    val name = ppi.name
                    val image = ppi.bufferedImage
                    currentX = (index % ceil) * (side.value + (extendPixel shl 1))
                    currentY = (index / ceil) * (side.value + (extendPixel shl 1))
                    atlasBufferImage.graphics.drawImage(image, currentX, currentY, null)
                    nameMap[name] = currentX to currentY
                }
                atlasBufferImage.graphics.dispose()
                atlases.add(
                    Atlas(
                        atlasBufferImage,
                        nameMap,
                        Size(images.size),
                        side,
                        Side(atlasSide),
                        Texture(atlasBufferImage)
                    )
                )
                val atlasSize = "${atlasSide}x$atlasSide"

                val writeFile = File("${System.getProperty("user.dir")}/temp/$name-$atlasSize.png")
                ImageIO.write(atlasBufferImage, "png", writeFile)
                logger.info("make texture atlas, size: $atlasSize, size: ${images.size}")
            }

            return TextureAtlas(atlases, extendPixel)
        }

        fun extendSide(sourceImage: BufferedImage, extendPixel: Int): BufferedImage {
            val finalImage: BufferedImage = BufferedImage(
                sourceImage.width + (extendPixel shl 1),
                sourceImage.height + (extendPixel shl 1),
                BufferedImage.TYPE_INT_ARGB
            )
            // 绘制图片
            val graphics: Graphics = finalImage.graphics
            graphics.drawImage(sourceImage, extendPixel, extendPixel, null)
            graphics.dispose()

            // 根据扩展的像素循环扩展边缘
            for (innerSide in 0 until extendPixel) {
                val currentSide = extendPixel - innerSide
                val rightSide = finalImage.width - currentSide - 1
                val bottomSide = finalImage.height - currentSide - 1

                val leftTopPoint: Int = // 左上点
                    finalImage.getRGB(currentSide, currentSide)
                val rightTopPoint: Int = // 右上点
                    finalImage.getRGB(rightSide, currentSide)
                val leftBottomPoint: Int = // 左下点
                    finalImage.getRGB(currentSide, bottomSide)
                val rightBottomPoint: Int =// 右下点
                    finalImage.getRGB(rightSide, bottomSide)

                val leftPoint = currentSide - 1
                val topPoint = currentSide - 1
                val rightPoint = finalImage.width - currentSide
                val bottomPoint = finalImage.height - currentSide
                finalImage.setRGB(leftPoint, topPoint, leftTopPoint)
                finalImage.setRGB(rightPoint, topPoint, rightTopPoint)
                finalImage.setRGB(leftPoint, bottomPoint, leftBottomPoint)
                finalImage.setRGB(rightPoint, bottomPoint, rightBottomPoint)

                // 左边
                for (index in 0..finalImage.height - 1 - (currentSide shl 1))
                    finalImage.setRGB(
                        leftPoint, index + currentSide,
                        finalImage.getRGB(currentSide, index + currentSide)
                    )
                // 右边
                for (index in 0..finalImage.height - 1 - (currentSide shl 1))
                    finalImage.setRGB(
                        rightPoint, index + currentSide,
                        finalImage.getRGB(rightSide, index + currentSide)
                    )
                // 顶边
                for (index in 0..finalImage.width - 1 - (currentSide shl 1))
                    finalImage.setRGB(
                        index + currentSide, topPoint,
                        finalImage.getRGB(index + currentSide, currentSide)
                    )
                // 底边
                for (index in 0..finalImage.width - 1 - (currentSide shl 1))
                    finalImage.setRGB(
                        index + currentSide, bottomPoint,
                        finalImage.getRGB(index + currentSide, rightSide)
                    )
            }

            return finalImage
        }
    }

    @JvmInline
    value class Side(val value: Int)

    @JvmInline
    value class Size(val value: Int)

    class PreProcessImage(val name: String, var bufferedImage: BufferedImage)

    class Atlas(
        val image: BufferedImage,
        val nameMap: Map<String, Pair<Int, Int>>,
        val size: Size,
        val imageSide: Side,
        val atlasSide: Side,
        val texture: Texture,
    )
}