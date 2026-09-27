package cn.afeibaili.gl.util

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


/**
 * # 临时文件工具类
 *
 * @author AfeiBaili
 * @version 2026/9/26 22:19
 */

object TempFileUtil {
    val tempDir = "${System.getProperty("user.dir")}/temp"
    val imageTempDir = "$tempDir/image"

    init {
        File(tempDir).mkdirs()
        File(imageTempDir).mkdirs()
    }

    fun createTempImageFile(image: BufferedImage, filename: String): File {
        val file = File(imageTempDir, filename)
        ImageIO.write(image, "png", file)
        return file
    }
}