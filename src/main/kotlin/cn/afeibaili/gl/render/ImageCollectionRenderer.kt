package cn.afeibaili.gl.render

import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.image.atlas.BigImageAtlas
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.layout.image.IconUpdater
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11C.GL_FLOAT
import org.lwjgl.opengl.GL44C.GL_DYNAMIC_STORAGE_BIT
import org.lwjgl.opengl.GL45C.*


/**
 * # 图片集合渲染器（静态渲染器）
 *
 * 渲染一个图片集合，增加渲染性能
 *
 * @author AfeiBaili
 * @version 2026/9/30 12:17
 */

class ImageCollectionRenderer(
    override val program: Program,
    override val camera: Camera,
    val bigImage: BigImageAtlas,
) : Renderable {
    val vao = glCreateVertexArrays()
    val positionsVbo = glCreateBuffers()
    val uvVbo = glCreateBuffers()
    val maxSize = 2024L
    val maxPositionByteSize = maxSize * 2 * 6 * Float.SIZE_BYTES
    val maxUvByteSize = maxSize * 2 * 6 * Float.SIZE_BYTES
    val positionBuffer = BufferUtils.createByteBuffer(maxPositionByteSize.toInt())
    val uvBuffer = BufferUtils.createByteBuffer(maxUvByteSize.toInt())
    var texture: Texture? = null
    val updaters = mutableSetOf<IconUpdater>()

    init {
        //位置信息 = 两个坐标 * 六个顶点
        glNamedBufferStorage(positionsVbo, maxPositionByteSize, GL_DYNAMIC_STORAGE_BIT)
        glVertexArrayVertexBuffer(vao, 0, positionsVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)

        //UV信息 = 两个uv * 六个顶点
        glNamedBufferStorage(uvVbo, maxUvByteSize, GL_DYNAMIC_STORAGE_BIT)
        glVertexArrayVertexBuffer(vao, 1, uvVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 1, 1)
        glEnableVertexArrayAttrib(vao, 1)
    }

    fun add(updater: IconUpdater) {
        updaters.add(updater)
    }

    fun remove(updater: IconUpdater) {
        updaters.remove(updater)
    }

    fun apply() {
        texture?.close()
        bigImage.apply()
        bigImage.generateUv()
        texture = bigImage.toTexture()
        texture!!.upload()
    }

    fun updateImagePosition() {
        positionBuffer.clear()
        val size: Int = updaters.sumOf { it.map.size }
        if (size > maxSize) throw IllegalStateException("图片数量超过最大值: $size")
        updaters.forEach {
            for (component in it.map.values) {
                val x0 = component.absoluteX
                val y0 = component.absoluteY
                val x1 = x0 + component.width
                val y1 = y0 + component.height
                positionBuffer.putFloat(x0).putFloat(y0)
                positionBuffer.putFloat(x1).putFloat(y0)
                positionBuffer.putFloat(x0).putFloat(y1)
                positionBuffer.putFloat(x1).putFloat(y0)
                positionBuffer.putFloat(x1).putFloat(y1)
                positionBuffer.putFloat(x0).putFloat(y1)
            }
        }
        positionBuffer.flip()
        uploadPositionBuffer()
    }

    fun updateImageUv() {
        uvBuffer.clear()
        val size: Int = updaters.sumOf { it.map.size }
        if (size > maxSize) throw IllegalStateException("图片数量超过最大值: $size")
        updaters.forEach {
            for (component in it.map.values) {
                val uv: FloatArray = component.image.uv
                val u0 = uv[0]
                val v0 = uv[1]
                val u1 = uv[2]
                val v1 = uv[3]
                uvBuffer.putFloat(u0).putFloat(v0)
                uvBuffer.putFloat(u1).putFloat(v0)
                uvBuffer.putFloat(u0).putFloat(v1)
                uvBuffer.putFloat(u1).putFloat(v0)
                uvBuffer.putFloat(u1).putFloat(v1)
                uvBuffer.putFloat(u0).putFloat(v1)
            }
        }
        uvBuffer.flip()
        uploadUvBuffer()
    }

    fun uploadPositionBuffer() {
        glNamedBufferSubData(positionsVbo, 0, positionBuffer)
    }

    fun uploadUvBuffer() {
        glNamedBufferSubData(uvVbo, 0, uvBuffer)
    }

    fun render() {
        program.use()
        camera.apply()
        texture!!.bind()
        updateImageUv()
        updateImagePosition()
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLES, 0, updaters.sumOf { it.map.size } * 6)
    }

    override fun close() {
        program.close()
        glDeleteVertexArrays(vao)
        glDeleteBuffers(positionsVbo)
        glDeleteBuffers(uvVbo)
    }
}