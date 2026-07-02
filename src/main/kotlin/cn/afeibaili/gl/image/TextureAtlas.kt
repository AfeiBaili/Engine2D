package cn.afeibaili.gl.image

import cn.afeibaili.gl.exception.ImageException
import cn.afeibaili.gl.exception.UnknownElementException
import cn.afeibaili.gl.image.PreProcessImageInfo.Companion.transform
import cn.afeibaili.gl.logger.LoggerFactory
import cn.afeibaili.gl.tool.Index
import cn.afeibaili.gl.tool.Side
import cn.afeibaili.gl.tool.Size
import cn.afeibaili.gl.tool.putElementOrCreateList
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.sqrt


/**
 * # 纹理图集
 *
 * 图集中可能包含动态纹理，动态纹理由 `Atlas` index来管理
 *
 * @param atlas 可能为不同大小的纹理集
 * @param extendPixel 扩展的像素大小
 * @see Atlas
 *
 * @author AfeiBaili
 * @version 2026/6/4 20:55
 */

class TextureAtlas(val atlas: Map<Index, Atlas>, val extendPixel: Int) {
    companion object {
        private val logger = LoggerFactory.create("TextureAtlas")

        /**
         * 创建纹理图集 (不过滤图片)
         *
         * 将文件列表的文件转换为一个大图
         *
         * @param textureId 文件名（id）
         * @param imageFiles 文件列表
         * @param extendPixel 防止纹理“流血”的扩展像素，默认为1
         * @param models 自定义纹理
         */
        fun create(
            textureId: String,
            imageFiles: List<File>,
            extendPixel: Int = 1,
            vararg models: TextureModel,
        ): TextureAtlas {
            //// 根据图片大小区分图集 ///////////////////////////
            val atlasMap = HashMap<Side, MutableList<PreProcessImageSet>>()  //根据图片大小分类图片
            val imageMap = HashMap<String, List<PreProcessImageInfo>>() //存放动态或静态图片

            // 文件图集
            imageFiles.forEach { file ->
                val (id, index) = runCatching {
                    val split: List<String> = file.name.split(".")
                    if (split.last() != "png") return@forEach
                    getIdAndIndex(split.first())
                }.getOrElse { exception ->
                    throw ImageException("图片获取id错误: ${exception.message}")
                }

                val readImageBuffer: BufferedImage = runCatching {
                    ImageIO.read(file)
                }.getOrElse { throw ImageException("此文件无法转为图片: ${file.canonicalPath}") }
                val width: Int = readImageBuffer.width
                val height: Int = readImageBuffer.height
                if (width != height) throw ImageException("图片宽高不一致，请用正方形图片")
                val side = Side(width)

                imageMap.putElementOrCreateList(id, Triple(index, readImageBuffer, side).transform())
            }
            // 自定义图集
            models.forEach { model ->
                val (id, index) = runCatching {
                    getIdAndIndex(model.id)
                }.getOrElse { exception ->
                    throw ImageException("图片获取id错误: ${exception.message}")
                }

                val width = model.image.width
                val height = model.image.height
                if (width != height) throw ImageException("图片宽高不一致，请用正方形图片")
                val side = Side(width)

                imageMap.putElementOrCreateList(id, Triple(index, model.image, side).transform())
            }
            //// 转换预处理图片，处理静态动态图片 //////////////////
            imageMap.forEach { (id, list) ->
                val groupBy = list.groupBy { it.side }
                if (groupBy.size != 1) throw ImageException("动态纹理请保持比例相同: $id")
                atlasMap.putElementOrCreateList(
                    groupBy.keys.toList()[0],
                    PreProcessImageSet(id, list.sortedBy { it.index.value })
                )
            }

            //// 扩展图片 /////////////////////////////////////
            atlasMap.forEach { (side, images) ->
                images.forEach { image ->
                    image.indexImageList.forEach {
                        it.image = extendSide(it.image, extendPixel)
                    }
                }
            }

            //// 转换图集 /////////////////////////////////////
            val atlases = mutableMapOf<Index, Atlas>()
            atlasMap.toList().forEachIndexed { atlasId, (side, images) ->
                val ceil = ceil(sqrt(images.sumOf { it.indexImageList.size }.toDouble())).toInt()
                val atlasSide: Int = ceil * side.value + ceil * (extendPixel shl 1)
                val atlasBufferImage = BufferedImage(atlasSide, atlasSide, BufferedImage.TYPE_INT_ARGB)
                val nameMap = mutableMapOf<String, List<Index>>()

                var currentX: Int
                var currentY: Int
                var index = 0
                images.forEach { ppis ->
                    ppis.indexImageList.forEach { ppii ->
                        val name = ppis.name
                        val image = ppii.image
                        currentX = (index % ceil) * (side.value + (extendPixel shl 1))
                        currentY = (index / ceil) * (side.value + (extendPixel shl 1))
                        atlasBufferImage.graphics.drawImage(image, currentX, currentY, null)
                        nameMap.putElementOrCreateList(name, Index(index))
                        index++
                    }
                }
                atlasBufferImage.graphics.dispose()
                atlases[Index(atlasId)] = Atlas(
                    Index(atlasId),
                    atlasBufferImage,
                    nameMap,
                    Size(images.sumOf { it.indexImageList.size }),
                    side,
                    Side(atlasSide),
                    Texture(atlasBufferImage),
                    ceil
                )
                val atlasSize = "${atlasSide}x$atlasSide"

                val writeFile = File("${System.getProperty("user.dir")}/temp/$textureId-$atlasSize.png")
                ImageIO.write(atlasBufferImage, "png", writeFile)
                logger.info("make texture atlas, size: $atlasSize, size: ${images.size}")
            }

            return TextureAtlas(atlases, extendPixel)
        }

        internal fun getIdAndIndex(name: String): Pair<String, Index> {
            val matchRegex = "(.*)_([0-9]+)".toRegex()
            val bool: Boolean = matchRegex.matches(name)
            if (!bool) return name to Index(0)
            val id: String = matchRegex.find(name)!!.groups[1]!!.value
            val index: Int = matchRegex.find(name)!!.groups[2]!!.value.toInt()
            return id to Index(index)
        }

        fun extendSide(sourceImage: BufferedImage, extendPixel: Int): BufferedImage {
            val finalImage = BufferedImage(
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
                for (index in 0..finalImage.height - 1 - (currentSide shl 1)) finalImage.setRGB(
                    leftPoint, index + currentSide, finalImage.getRGB(currentSide, index + currentSide)
                )
                // 右边
                for (index in 0..finalImage.height - 1 - (currentSide shl 1)) finalImage.setRGB(
                    rightPoint, index + currentSide, finalImage.getRGB(rightSide, index + currentSide)
                )
                // 顶边
                for (index in 0..finalImage.width - 1 - (currentSide shl 1)) finalImage.setRGB(
                    index + currentSide, topPoint, finalImage.getRGB(index + currentSide, currentSide)
                )
                // 底边
                for (index in 0..finalImage.width - 1 - (currentSide shl 1)) finalImage.setRGB(
                    index + currentSide, bottomPoint, finalImage.getRGB(index + currentSide, bottomSide)
                )
            }

            return finalImage
        }
    }

    /**
     * 获取纹理UV坐标
     *
     * @param id 图片id
     * @param uv 数组，长度需为4
     */
    fun getUvs(id: String): List<FloatArray> {
        val atlas: Atlas? = getAtlas(id)
        if (atlas == null)
            return throw UnknownElementException("未知的id，找不到uv: $id")
        return getUvByAtlas(id, atlas)
    }

    fun getUvByAtlas(id: String, atlas: Atlas): List<FloatArray> {
        val textureIndexs = atlas.textureNameMap[id]!!
        val list: MutableList<FloatArray> = mutableListOf()

        textureIndexs.forEach { it ->
            val index = it.value
            val uv = FloatArray(4)
            val column = index % atlas.rowLength
            val row = index / atlas.rowLength
            val x = (column * atlas.textureSide.value) + extendPixel + (column * (extendPixel shl 1))
            val y = (row * atlas.textureSide.value) + extendPixel + (row * (extendPixel shl 1))
            val atlasSide = atlas.atlasSide.value.toFloat()
            val imageSide = atlas.textureSide.value.toFloat()
            uv[0] = x.toFloat() / atlasSide
            uv[1] = y.toFloat() / atlasSide
            uv[2] = (x.toFloat() + imageSide) / atlasSide
            uv[3] = (y.toFloat() + imageSide) / atlasSide
            list.add(uv)
        }
        return list
    }

    fun getAtlas(id: String): Atlas? = atlas.values.find { it.textureNameMap[id] != null }
}