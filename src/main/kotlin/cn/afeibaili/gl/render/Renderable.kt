package cn.afeibaili.gl.render

import cn.afeibaili.gl.render.camera.Camera
import cn.afeibaili.gl.render.shader.Program
import java.io.Closeable


/**
 * # 渲染器接口
 *
 * @author AfeiBaili
 * @version 2026/8/12 12:12
 */

interface Renderable : Closeable {
    val program: Program
    val camera: Camera
}