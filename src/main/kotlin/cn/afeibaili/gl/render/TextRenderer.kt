package cn.afeibaili.gl.render

import cn.afeibaili.gl.font.Character
import cn.afeibaili.gl.font.Font
import cn.afeibaili.gl.font.Text
import cn.afeibaili.gl.font.TextList
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.opengl.GL45C.*
import java.nio.ByteBuffer


/**
 * # 文本渲染器
 *
 * @author AfeiBaili
 * @version 2026/7/8 18:02
 */

class TextRenderer(val font: Font, override val program: Program, override val camera: Camera) : Renderable {
    //fixme 请优化为常驻内存
    val textList = TextList()
    val vao = glCreateVertexArrays()
    val vbo = glCreateBuffers()

    private var bytePreChar = Float.SIZE_BYTES * 4 * 6
    private var maxCharSize: Long = 255L

    init {
        font.texture.upload()
        glNamedBufferStorage(vbo, maxCharSize * bytePreChar, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 4 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribBinding(vao, 1, 0)
        glEnableVertexArrayAttrib(vao, 1)
    }

    fun update(vararg text: Text) {
        text.forEach { textList.add(it) }
    }

    fun render() {
        //todo 如果字符数达到255重新接着渲染
        if (textList.isEmpty()) return
        program.use()
        camera.apply()
        font.texture.bind()
        glBindVertexArray(vao)
        val bb: ByteBuffer = glMapNamedBuffer(vbo, GL_WRITE_ONLY) ?: return
        bb.clear()
        var totalVertices = 0
        textList.forEach { text ->
            var currentX: Float = text.x
            val currentY: Float = text.y
            text.string.forEach { char ->
                val character: Character? = font.getChar(char)
                if (character == null) return@forEach
                if (character.height == 0f) {
                    currentX += character.width
                }

                val u0 = character.uv[0] //左
                val v0 = character.uv[1] //上
                val u1 = character.uv[2] //右
                val v1 = character.uv[3] //下

                //p1
                bb.putFloat(currentX)
                    .putFloat(currentY)
                    .putFloat(u0)
                    .putFloat(v1)
                //p2
                bb.putFloat(currentX + character.width)
                    .putFloat(currentY)
                    .putFloat(u1)
                    .putFloat(v1)
                //p3
                bb.putFloat(currentX)
                    .putFloat(currentY + character.height)
                    .putFloat(u0)
                    .putFloat(v0)
                //p4
                bb.putFloat(currentX + character.width)
                    .putFloat(currentY)
                    .putFloat(u1)
                    .putFloat(v1)
                //p5
                bb.putFloat(currentX + character.width)
                    .putFloat(currentY + character.height)
                    .putFloat(u1)
                    .putFloat(v0)
                //p6
                bb.putFloat(currentX)
                    .putFloat(currentY + character.height)
                    .putFloat(u0)
                    .putFloat(v0)
                currentX += character.width
                totalVertices += 6
            }
        }
        glUnmapNamedBuffer(vbo)
        glDrawArrays(GL_TRIANGLES, 0, totalVertices)
        textList.clear()
    }

    override fun close() {
        program.close()
        glDeleteBuffers(vbo)
        glDeleteVertexArrays(vao)
    }
}