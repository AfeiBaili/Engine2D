package cn.afeibaili.gl.render.shader

import cn.afeibaili.gl.exception.ShaderException
import cn.afeibaili.gl.logger.LoggerFactory
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20C.GL_LINK_STATUS
import org.lwjgl.opengl.GL45C
import org.lwjgl.system.MemoryStack
import java.io.Closeable
import java.nio.FloatBuffer


/**
 * # 着色器程序
 *
 * @author AfeiBaili
 * @version 2026/6/5 11:49
 */

class Program private constructor(val programLocation: Int, val shaders: Array<out Shader>) : Closeable {
    private var isLinked = false
    fun link() {
        GL45C.glLinkProgram(programLocation)
        val pi: Int = GL45C.glGetProgrami(programLocation, GL_LINK_STATUS)
        check(pi == GL45C.GL_TRUE) {
            val log: String = GL45C.glGetProgramInfoLog(programLocation)
            logger.error("failed to link shader: $log")
            log
        }
        isLinked = true
        logger.info("linked program...")
    }

    fun use() {
        if (!isLinked) throw ShaderException("program is not linked")
        GL45C.glUseProgram(programLocation)
    }

    fun setUniform(
        name: String,
        f1: Float? = null,
        i1: Int? = null,
        m4f: Matrix4f? = null,
    ) {
        use()
        val location: Int = GL45C.glGetUniformLocation(programLocation, name)
        MemoryStack.stackPush().use { stack ->
            when {
                f1 != null -> GL45C.glUniform1f(location, f1)
                i1 != null -> GL45C.glUniform1i(location, i1)
                m4f != null -> {
                    val data: FloatBuffer = stack.mallocFloat(4 * 4)
                    m4f.get(data)
                    GL45C.glUniformMatrix4fv(location, false, data)
                }
            }
        }
    }

    override fun close() {
        GL45C.glDeleteProgram(programLocation)
        shaders.forEach { it.close() }
    }

    companion object {
        private val logger = LoggerFactory.create("Program")

        fun create(vararg shaders: Shader): Program {
            val program: Int = GL45C.glCreateProgram()
            for (shader in shaders) {
                GL45C.glAttachShader(program, shader.shaderLocation)
            }
            return Program(program, shaders)
        }
    }
}