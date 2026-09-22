package cn.afeibaili.gl.util

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object ImageUtil {
    fun loadImage(filepath: String): BufferedImage {
        return runCatching {
            ImageIO.read(File(filepath))
        }.getOrElse {
            throw IllegalArgumentException("无法读取图片数据: $filepath, 原因: ${it.message}")
        }
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