package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.opengl.GL45C.*
import java.nio.ByteBuffer


/**
 * # 世界渲染器
 *
 * 需要具体实现更新方法
 *
 * @author AfeiBaili
 * @version 2026/8/30 12:56
 */

abstract class WorldRenderer(
    override val program: Program,
    override val camera: Camera,
) : Renderable {
    val vao = glCreateVertexArrays()
    val vbo = glCreateBuffers()
    val instanceVbo = glCreateBuffers()
    val uvVbo = glCreateBuffers()

    init {
        glNamedBufferStorage(vbo, vertices, 0)
        glVertexArrayVertexBuffer(vao, 0, vbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)

        glNamedBufferStorage(
            instanceVbo, BLOCK_SIZE * 2L * Float.SIZE_BYTES, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT
        )
        glVertexArrayVertexBuffer(vao, 1, instanceVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 1, 1)
        glEnableVertexArrayAttrib(vao, 1)
        glVertexArrayBindingDivisor(vao, 1, 1)

        glNamedBufferStorage(
            uvVbo, BLOCK_SIZE * UV_SIZE.toLong() * Float.SIZE_BYTES, GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT
        )
        glVertexArrayVertexBuffer(vao, 2, uvVbo, 0, UV_SIZE * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 2, UV_SIZE, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 2, 2)
        glEnableVertexArrayAttrib(vao, 2)
        glVertexArrayBindingDivisor(vao, 2, 1)
    }

    fun getInstanceBuffer(): ByteBuffer? {
        return glMapNamedBuffer(instanceVbo, GL_WRITE_ONLY)
    }

    fun getUvBuffer(): ByteBuffer? {
        return glMapNamedBuffer(uvVbo, GL_WRITE_ONLY)
    }

    fun putBuffer() {
        glUnmapNamedBuffer(instanceVbo)
        glUnmapNamedBuffer(uvVbo)
    }

    /**
     * ## 使用更新方法将数据存入至uv缓存和实例缓存
     *
     * 获取实例和uv缓存需要调用`getInstanceBuffer()`和`getUvBuffer()`
     *
     * 随后调用`putBuffer()`方法上传数据至GPU
     *
     *  @return 实例数量
     */
    fun renderBatch(instanceSize: Int) {
        program.use()
        camera.apply()
        glBindVertexArray(vao)
        glDrawArraysInstanced(GL_TRIANGLES, 0, 6, instanceSize)
    }

    override fun close() {
        program.close()
        glDeleteVertexArrays(vao)
        glDeleteBuffers(vbo)
        glDeleteBuffers(uvVbo)
        glDeleteBuffers(instanceVbo)
    }

    companion object {
        const val UV_SIZE = 4
        const val BLOCK_SIZE = 1024 shl 4

        val vertices = floatArrayOf(
            0f, 0f,
            1f, 0f,
            0f, 1f,

            1f, 0f,
            1f, 1f,
            0f, 1f,
        )
    }
}