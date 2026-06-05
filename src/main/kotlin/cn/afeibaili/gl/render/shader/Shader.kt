package cn.afeibaili.gl.render.shader

import cn.afeibaili.gl.logger.LoggerFactory
import org.lwjgl.opengl.GL45C
import java.io.Closeable


/**
 * # 着色器实例
 *
 * @author AfeiBaili
 * @version 2026/6/5 11:49
 */

class Shader private constructor(val shaderLocation: Int, val source: String) : Closeable {

    override fun close() {
        GL45C.glDeleteShader(shaderLocation)
    }

    companion object {
        private val logger = LoggerFactory.create("Shader")

        fun create(type: ShaderType, source: String): Shader {
            val shader: Int = GL45C.glCreateShader(type.value)
            GL45C.glShaderSource(shader, source)
            GL45C.glCompileShader(shader)
            val si: Int = GL45C.glGetShaderi(shader, GL45C.GL_COMPILE_STATUS)
            check(si == GL45C.GL_TRUE) {
                val log: String = GL45C.glGetShaderInfoLog(shader)
                logger.error("failed to compile shader: $log")
                log
            }
            logger.info("compilation success")
            return Shader(shader, source)
        }
    }


    enum class ShaderType(val value: Int) {
        VERTEX(GL45C.GL_VERTEX_SHADER),
        FRAGMENT(GL45C.GL_FRAGMENT_SHADER),
        ;
    }
}