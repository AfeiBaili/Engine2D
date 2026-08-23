package cn.afeibaili.gl.render

import cn.afeibaili.gl.font.Character
import cn.afeibaili.gl.font.Font
import cn.afeibaili.gl.font.Text
import cn.afeibaili.gl.font.TextMap
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
    val textMap = TextMap()
    val vao = glCreateVertexArrays()
    val vbo = glCreateBuffers()

    private var bytePreChar = Float.SIZE_BYTES * 4 * 6
    private var maxCharSize: Int = 255

    init {
        font.texture.upload()
        glNamedBufferStorage(vbo, maxCharSize.toLong() * bytePreChar, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 4 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribBinding(vao, 1, 0)
        glEnableVertexArrayAttrib(vao, 1)
    }

    fun update(vararg text: Text) {
        text.forEach { textMap.put(it) }
    }

    fun render() {
        if (textMap.isEmpty()) return
        program.use()
        camera.apply()
        font.texture.bind()
        glBindVertexArray(vao)
        val bb: ByteBuffer = glMapNamedBuffer(vbo, GL_WRITE_ONLY) ?: return
        bb.clear()
        var totalVertices = 0
        var charIndex = 0
        textMap.forEach { text ->
            var currentX: Float = text.x
            val currentY: Float = text.y
            val textScale = text.scale
            text.string.forEach { char ->
                if (charIndex % maxCharSize == 0 && charIndex != 0) {
                    glUnmapNamedBuffer(vbo)
                    glDrawArrays(GL_TRIANGLES, 0, totalVertices)
                    bb.clear()
                    totalVertices = 0
                    charIndex = 0
                }

                val character: Character? = font.getChar(char)
                if (character == null) return@forEach
                if (character.height == 0f) {
                    currentX += character.width * textScale
                    return@forEach
                }

                val x0 = currentX
                val y0 = currentY
                val x1 = currentX + character.width * textScale
                val y1 = currentY + character.height * textScale

                val u0 = character.uv[0] //左
                val v0 = character.uv[1] //上
                val u1 = character.uv[2] //右
                val v1 = character.uv[3] //下

                bb.putFloat(x0).putFloat(y0).putFloat(u0).putFloat(v1)
                bb.putFloat(x1).putFloat(y0).putFloat(u1).putFloat(v1)
                bb.putFloat(x0).putFloat(y1).putFloat(u0).putFloat(v0)
                bb.putFloat(x1).putFloat(y0).putFloat(u1).putFloat(v1)
                bb.putFloat(x1).putFloat(y1).putFloat(u1).putFloat(v0)
                bb.putFloat(x0).putFloat(y1).putFloat(u0).putFloat(v0)
                currentX += character.width * textScale
                totalVertices += 6
                charIndex++
            }
        }
        glUnmapNamedBuffer(vbo)
        glDrawArrays(GL_TRIANGLES, 0, totalVertices)
        textMap.clear()
    }

    override fun close() {
        program.close()
        glDeleteBuffers(vbo)
        glDeleteVertexArrays(vao)
    }
}