package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.opengl.GL15C.glDeleteBuffers
import org.lwjgl.opengl.GL30C.glDeleteVertexArrays
import org.lwjgl.opengl.GL45C.*
import java.io.Closeable
import java.nio.ByteBuffer


/**
 * # 网格渲染器
 *
 * @author AfeiBaili
 * @version 2026/6/3 21:47
 */

class GridRenderer(val program: Program, val camera: Camera, val blockSize: Int = 1024) : Renderable, Closeable {
    val vao: Int = glCreateVertexArrays()
    val uvSize = 4
    val verticesVbo: Int = glCreateBuffers()
    val instanceVbo: Int = glCreateBuffers()
    val uvVbo = glCreateBuffers()

    init {
        glNamedBufferStorage(verticesVbo, vertices, 0)
        glVertexArrayVertexBuffer(vao, 0, verticesVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)

        glNamedBufferStorage(
            instanceVbo, blockSize.toLong() * 2 * Float.SIZE_BYTES,
            GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT
        )
        glVertexArrayVertexBuffer(vao, 1, instanceVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 1, 1)
        glEnableVertexArrayAttrib(vao, 1)
        glVertexArrayBindingDivisor(vao, 1, 1)

        glNamedBufferStorage(
            uvVbo, blockSize.toLong() * uvSize * Float.SIZE_BYTES,
            GL_DYNAMIC_STORAGE_BIT or GL_MAP_WRITE_BIT
        )
        glVertexArrayVertexBuffer(vao, 2, uvVbo, 0, uvSize * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 2, uvSize, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 2, 2)
        glEnableVertexArrayAttrib(vao, 2)
        glVertexArrayBindingDivisor(vao, 2, 1)
    }

    inline fun renderGrid(updateInstanceData: ByteBuffer.() -> Unit, updateUvData: ByteBuffer.() -> Unit, instanceSize: Int) {
        val instanceMem = glMapNamedBuffer(instanceVbo, GL_WRITE_ONLY) ?: return
        val uvMem = glMapNamedBuffer(uvVbo, GL_WRITE_ONLY) ?: return

        uvMem.clear()
        instanceMem.clear()

        updateInstanceData(instanceMem)
        updateUvData(uvMem)

        glUnmapNamedBuffer(instanceVbo)
        glUnmapNamedBuffer(uvVbo)

        program.use()
        glBindVertexArray(vao)
        glDrawArraysInstanced(GL_TRIANGLES, 0, 6, instanceSize)
        glBindVertexArray(0)
    }

    override fun close() {
        glUnmapNamedBuffer(uvVbo)
        glDeleteVertexArrays(vao)
        glDeleteBuffers(uvVbo)
        glDeleteBuffers(verticesVbo)
    }

    companion object {
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