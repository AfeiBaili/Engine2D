package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.Color.Companion.putColor
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.layout.shape.border.BorderComponent
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45C.*


/**
 * # 边框渲染器
 *
 * @author AfeiBaili
 * @version 2026/10/8 14:13
 */

class BorderRenderer(
    override val program: Program,
    override val camera: Camera,
) : Renderable {
    val vao = glCreateVertexArrays()
    val vbo = glCreateBuffers()
    val maxSize = 1024

    // buffer 大小 = 最大数量 * ((两个顶点属性 * 八个三角形 * 三个顶点) + 四个颜色) * Float.SIZE_BYTES
    val maxBufferSize = maxSize * ((2 * 8 * 3) + 4) * Float.SIZE_BYTES
    val buffer = BufferUtils.createByteBuffer(maxBufferSize)
    val borders = mutableSetOf<BorderComponent>()

    init {
        glNamedBufferStorage(vbo, maxBufferSize.toLong(), GL_DYNAMIC_STORAGE_BIT)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 2 * Float.SIZE_BYTES + 4)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)

        glVertexArrayAttribFormat(vao, 1, 4, GL_UNSIGNED_BYTE, true, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribBinding(vao, 1, 0)
        glEnableVertexArrayAttrib(vao, 1)
    }

    fun add(border: BorderComponent) {
        borders.add(border)
    }

    fun clear() {
        borders.clear()
    }

    fun render() {
        if (borders.isEmpty()) return
        if (borders.size > maxSize) error("超过Border内存最大分配, 边框太多: ${borders.size}")
        buffer.clear()
        program.use()
        camera.apply()
        for (component in borders) {
            val borderWidth: Float = component.borderWidth
            val color: Color = component.color
            val x = component.absoluteX
            val y = component.absoluteY
            val width = component.width
            val height = component.height
            val tx0 = x
            val ty0 = y
            val tx1 = x + width - borderWidth
            val ty1 = y + borderWidth
            val rx0 = x + width
            val ry0 = y
            val rx1 = x + width - borderWidth
            val ry1 = y + height - borderWidth
            val bx0 = x + width
            val by0 = y + height
            val bx1 = x + borderWidth
            val by1 = y + height - borderWidth
            val lx0 = x
            val ly0 = y + height
            val lx1 = x + borderWidth
            val ly1 = y + borderWidth
            putTriangleFloat(tx0, ty0, tx1, ty0, tx1, ty1, color)
            putTriangleFloat(tx0, ty0, tx0, ty1, tx1, ty1, color)
            putTriangleFloat(rx0, ry0, rx1, ry0, rx1, ry1, color)
            putTriangleFloat(rx0, ry0, rx0, ry1, rx1, ry1, color)
            putTriangleFloat(bx0, by0, bx1, by0, bx1, by1, color)
            putTriangleFloat(bx0, by0, bx0, by1, bx1, by1, color)
            putTriangleFloat(lx0, ly0, lx1, ly0, lx1, ly1, color)
            putTriangleFloat(lx0, ly0, lx0, ly1, lx1, ly1, color)
        }
        buffer.flip()
        glNamedBufferSubData(vbo, 0, buffer)
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, borders.size * 8 * 3)
    }

    override fun close() {
        borders.clear()
        program.close()
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
    }

    private fun putTriangleFloat(p1x: Float, p1y: Float, p2x: Float, p2y: Float, p3x: Float, p4y: Float, color: Color) {
        buffer.putFloat(p1x).putFloat(p1y).putColor(color)
            .putFloat(p2x).putFloat(p2y).putColor(color)
            .putFloat(p3x).putFloat(p4y).putColor(color)
    }
}