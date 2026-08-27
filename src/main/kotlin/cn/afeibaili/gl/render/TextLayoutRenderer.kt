package cn.afeibaili.gl.render

import cn.afeibaili.gl.font.Character
import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.layout.text.TextUpdater
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.opengl.GL45C.*
import java.nio.ByteBuffer


/**
 * # 文本布局渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/23 11:00
 */

class TextLayoutRenderer(
    override val program: Program,
    override val camera: Camera,
) : Renderable {
    private val textSet = mutableSetOf<TextUpdater>()
    val vao = glCreateVertexArrays()
    val vbo = glCreateBuffers()
    var showableBackground = false

    // 一个字符 = 四个Float字节 + 四个背景色字节 + 四个颜色字节 * 六个顶点
    private val bytePreChar = ((Float.SIZE_BYTES * 4) + 4 + 4) * 6
    private var maxSize = 1024

    init {
        glNamedBufferStorage(vbo, maxSize.toLong() * bytePreChar, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, Float.SIZE_BYTES * 4 + 4 + 4)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, Float.SIZE_BYTES * 2)
        glVertexArrayAttribBinding(vao, 1, 0)
        glEnableVertexArrayAttrib(vao, 1)
        glVertexArrayAttribFormat(vao, 2, 4, GL_UNSIGNED_BYTE, true, Float.SIZE_BYTES * 4)
        glVertexArrayAttribBinding(vao, 2, 0)
        glEnableVertexArrayAttrib(vao, 2)
        glVertexArrayAttribFormat(vao, 3, 4, GL_UNSIGNED_BYTE, true, Float.SIZE_BYTES * 4 + 4)
        glVertexArrayAttribBinding(vao, 3, 0)
        glEnableVertexArrayAttrib(vao, 3)
    }

    fun upload(updater: TextUpdater) {
        textSet.add(updater)
    }

    fun unload(updater: TextUpdater) {
        textSet.remove(updater)
    }

    fun clear() {
        textSet.clear()
    }

    fun render() {
        if (textSet.isEmpty()) return
        var lastTexture: Texture? = null
        program.use()
        camera.apply()
        glBindVertexArray(vao)
        val bb: ByteBuffer = glMapNamedBuffer(vbo, GL_WRITE_ONLY) ?: return
        bb.clear()
        var totalVertices = 0
        var charIndex = 0
        textSet.forEach { updater ->
            updater.forEach { text ->
                if (!text.showable) return@forEach
                if (lastTexture == null || lastTexture != text.font.texture) {
                    text.font.texture.bind()
                    lastTexture = text.font.texture
                }
                var currentX = text.absoluteX
                val currentY = text.absoluteY
                val textScale = text.scale
                text.string.forEach { char ->
                    if (charIndex >= maxSize) {
                        glUnmapNamedBuffer(vbo)
                        glDrawArrays(GL_TRIANGLES, 0, totalVertices)
                        bb.clear()
                        totalVertices = 0
                        charIndex = 0
                    }
                    val character: Character = text.font.getChar(char) ?: return@forEach
                    if (character.height == 0f) {
                        currentX += character.advanceX * textScale
                        return@forEach
                    }
                    val color = text.color
                    val backgroundColor = if (showableBackground) text.backgroundColor else Color.NONE
                    val baseline = currentY + text.font.ascent * textScale

                    val x0 = currentX + character.bearingX * textScale
                    val y0 = baseline - character.bearingY * textScale
                    val x1 = x0 + character.width * textScale
                    val y1 = y0 + character.height * textScale
                    val u0 = character.uv[0]
                    val v0 = character.uv[3]
                    val u1 = character.uv[2]
                    val v1 = character.uv[1]

                    bb.putFloat(x0).putFloat(y0).putFloat(u0).putFloat(v1)
                    color.get(bb)
                    backgroundColor.get(bb)
                    bb.putFloat(x1).putFloat(y0).putFloat(u1).putFloat(v1)
                    color.get(bb)
                    backgroundColor.get(bb)
                    bb.putFloat(x0).putFloat(y1).putFloat(u0).putFloat(v0)
                    color.get(bb)
                    backgroundColor.get(bb)
                    bb.putFloat(x1).putFloat(y0).putFloat(u1).putFloat(v1)
                    color.get(bb)
                    backgroundColor.get(bb)
                    bb.putFloat(x1).putFloat(y1).putFloat(u1).putFloat(v0)
                    color.get(bb)
                    backgroundColor.get(bb)
                    bb.putFloat(x0).putFloat(y1).putFloat(u0).putFloat(v0)
                    color.get(bb)
                    backgroundColor.get(bb)
                    currentX += character.advanceX * textScale
                    totalVertices += 6
                    charIndex++
                }
            }
        }
        glUnmapNamedBuffer(vbo)
        glDrawArrays(GL_TRIANGLES, 0, totalVertices)
    }

    override fun close() {
        program.close()
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
    }
}