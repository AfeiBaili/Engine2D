import cn.afeibaili.gl.image.TextureAtlas
import java.io.File
import kotlin.test.Test

/**
 * 测试纹理图集
 *
 * @author AfeiBaili
 * @version 2026/6/16 20:27
 */

class TestAtlas {
    @Test
    fun test01() {
        val inFile = File("B:\\Java\\Kotlin\\JumpJump\\resource\\tile")
        val files: List<File> = inFile.listFiles().toList()

        val atlas: TextureAtlas = TextureAtlas.create("test", files)
        val uv = FloatArray(4)
        atlas.getUv("dirt", uv)
        println(uv.joinToString("、"))
    }
}