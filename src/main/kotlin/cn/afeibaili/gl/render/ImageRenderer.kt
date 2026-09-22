package cn.afeibaili.gl.render

import cn.afeibaili.gl.image.Texture
import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.layout.image.AbstractImageComponent
import cn.afeibaili.gl.render.shader.Program
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL45C.*


/**
 * # 图片渲染器
 *
 * @author AfeiBaili
 * @version 2026/9/3 20:46
 */

class ImageRenderer(
    override val program: Program,
    override val camera: Camera,
) : Renderable {
    val vao = glCreateVertexArrays()
    val instanceVbo = glCreateBuffers()
    val uvVbo = glCreateBuffers()
    val maxSize = 1024L
    val maxInstanceByteSize = maxSize * 2 * 4 * Float.SIZE_BYTES
    val maxUvByteSize = maxSize * 2 * 4 * Float.SIZE_BYTES
    val instanceBuffer = BufferUtils.createByteBuffer(maxInstanceByteSize.toInt())
    val uvBuffer = BufferUtils.createByteBuffer(maxUvByteSize.toInt())
    val imageSet = mutableSetOf<AbstractImageComponent>()
    var texture: Texture? = null

    init {
        //一个instanceVbo = 两个坐标 * 四个顶点
        glNamedBufferStorage(instanceVbo, maxInstanceByteSize, GL_DYNAMIC_STORAGE_BIT)
        glVertexArrayVertexBuffer(vao, 0, instanceVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 0, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 0, 0)
        glEnableVertexArrayAttrib(vao, 0)

        //一个uvVbo = 两个uv * 四个顶点
        glNamedBufferStorage(uvVbo, maxUvByteSize, GL_DYNAMIC_STORAGE_BIT)
        glVertexArrayVertexBuffer(vao, 1, uvVbo, 0, 2 * Float.SIZE_BYTES)
        glVertexArrayAttribFormat(vao, 1, 2, GL_FLOAT, false, 0)
        glVertexArrayAttribBinding(vao, 1, 1)
        glEnableVertexArrayAttrib(vao, 1)
    }


    fun addImage(vararg image: AbstractImageComponent) {
        for (component in image) {
            imageSet.add(component)
        }
        texture?.close()
        texture = update(imageSet)
        texture!!.upload()
        uploadUvBuffer()
        uploadInstanceBuffer()
    }

    fun updateImagePosition() {
        var index = 0
        for (component in imageSet) {
            if (index >= maxSize) {
                throw IllegalStateException("图片数量超过最大值: $index")
            }
            val x0 = component.absoluteX
            val y0 = component.absoluteY
            val x1 = x0 + component.width
            val y1 = y0 + component.height
            uvBuffer.putFloat(x0).putFloat(y0)
            uvBuffer.putFloat(x1).putFloat(y0)
            uvBuffer.putFloat(x1).putFloat(y1)
            uvBuffer.putFloat(x0).putFloat(y1)
            index++
        }
        uploadInstanceBuffer()
    }

    fun updateImageUv() {
        var index = 0
        for (component in imageSet) {
            if (index >= maxSize) {
                throw IllegalStateException("图片数量超过最大值: $index")
            }
            val uv: FloatArray = component.uv
            val u0 = uv[0]
            val v0 = uv[1]
            val u1 = uv[2]
            val v1 = uv[3]
            uvBuffer.putFloat(u0).putFloat(v0)
            uvBuffer.putFloat(u1).putFloat(v0)
            uvBuffer.putFloat(u1).putFloat(v1)
            uvBuffer.putFloat(u0).putFloat(v1)
            index++
        }
        uploadUvBuffer()
    }

    fun uploadInstanceBuffer() {
        glNamedBufferSubData(instanceVbo, 0, instanceBuffer)
    }

    fun uploadUvBuffer() {
        glNamedBufferSubData(uvVbo, 0, uvBuffer)
    }

    fun render() {
        program.use()
        camera.apply()
        texture?.bind()
        glBindVertexArray(vao)
        glDrawArrays(GL_TRIANGLE_FAN, 0, imageSet.size * 4)
    }

    override fun close() {
        program.close()
        imageSet.clear()
        glDeleteBuffers(instanceVbo)
        glDeleteBuffers(uvVbo)
        glDeleteVertexArrays(vao)
    }

    companion object ImageAtlas {
        fun update(imageSet: Set<AbstractImageComponent>): Texture {
            TODO()
        }
    }
}