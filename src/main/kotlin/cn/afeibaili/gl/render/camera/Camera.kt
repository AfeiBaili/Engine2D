package cn.afeibaili.gl.render.camera

import cn.afeibaili.gl.render.shader.Program
import org.joml.Matrix4f

/**
 * # 相机类
 *
 * @author AfeiBaili
 * @version 2026/6/6 16:59
 */

class Camera(val program: Program, val projectionName: String = "projection", val viewName: String = "view") {
    val zoom = 10f
    var scale = 1f
    val projection: Matrix4f = Matrix4f().ortho(0f, zoom, 0f, zoom, -1f, 1f)
    val view = Matrix4f()

    private var x = 0f
    private var y = 0f
    private var left = 0f
    private var right = 0f
    private var bottom = 0f
    private var top = 0f
    private var rotate = 0f

    // VIEW ////
    fun scale(scale: Float): Camera {
        this.scale = scale
        return this
    }

    fun translation(x: Float, y: Float): Camera {
        this.x = x
        this.y = y
        return this
    }

    fun rotate(angle: Float): Camera {
        this.rotate = angle
        return this
    }

    fun updateView() {
        val centerX = zoom / 2f
        val centerY = zoom / 2f

        view.identity()
            .translate(centerX + x, centerY + y, 0f)
            .scale(scale, scale, 1f)
            .translate(-centerX, -centerY, 0f)
    }

    // PROJECTION ////
    fun ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float = -1f, far: Float = 1f): Camera {
        this.left = left
        this.right = right
        this.bottom = bottom
        this.top = top
        projection.setOrtho(left, right, bottom, top, near, far)
        return this
    }

    fun apply() {
        updateView()
        uploadUniform(program, projectionName, viewName)
    }

    private fun uploadUniform(program: Program, projectionName: String = "projection", viewName: String = "view") {
        program.setUniform(projectionName, m4f = projection)
        program.setUniform(viewName, m4f = view)
    }
}