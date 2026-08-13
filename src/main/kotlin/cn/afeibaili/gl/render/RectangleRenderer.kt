package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.opengl.GL45C.*
import java.nio.ByteBuffer

/**
 * # 矩形渲染器
 *
 * @author AfeiBaili
 * @version 2026/8/12 12:11
 */

class RectangleRenderer(override val program: Program, override val camera: Camera) : Renderable {
    private val vao = glCreateVertexArrays()
    private val vbo = glCreateBuffers()
    val rectangleMaxSize = 100L
    private val rectangleMap = mutableMapOf<String, Rectangle>()
    private val rectangles get() = rectangleMap.values

    init {
        glNamedBufferStorage(vbo, rectangleMaxSize * 4 * Float.SIZE_BYTES, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 4 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 4, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glVertexArrayBindingDivisor(vao, 0, 1)
        glEnableVertexArrayAttrib(vao, 0)
    }

    fun put(key: String, x: Float, y: Float, w: Float, h: Float) {
        rectangleMap[key] = Rectangle(key, x, y, w, h)
    }

    fun render() {
        program.use()
        camera.apply()
        glBindVertexArray(vao)
        val bb: ByteBuffer = glMapNamedBuffer(vbo, GL_WRITE_ONLY) ?: return
        bb.clear()
        rectangles.forEachIndexed { index, rectangle ->
            if (index % 100 == 0 && index != 0) {
                glUnmapNamedBuffer(vbo)
                glDrawArraysInstanced(GL_TRIANGLE_FAN, 0, 4, rectangles.size)
                bb.clear()
            } else {
                bb.putFloat(rectangle.x)
                    .putFloat(rectangle.y)
                    .putFloat(rectangle.width)
                    .putFloat(rectangle.height)
            }

        }
        glUnmapNamedBuffer(vbo)
        glDrawArraysInstanced(GL_TRIANGLE_FAN, 0, 4, rectangles.size)
    }

    override fun close() {
        program.close()
        rectangleMap.clear()
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
    }
}