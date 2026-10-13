package cn.afeibaili.gl.input


/**
 * # 按键绑定
 *
 * @author AfeiBaili
 * @version 2026/9/2 12:17
 */

class MouseButton(
    val id: String,
    val glfwButton: Int,
    val alt: Boolean = false,
    val ctrl: Boolean = false,
    val shift: Boolean = false,
)