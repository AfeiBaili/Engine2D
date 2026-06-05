package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.shader.Program
import org.joml.Matrix4f
import org.lwjgl.opengl.GL45C
import org.lwjgl.system.MemoryStack
import java.nio.FloatBuffer

fun setUniform(
    program: Program, name: String,
    f1: Float? = null,
    i1: Int? = null,
    m4f: Matrix4f? = null,
) {
    val location: Int = GL45C.glGetUniformLocation(program.programLocation, name)
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