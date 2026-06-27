package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # 按键回调
 *
 * @author AfeiBaili
 * @version 2026/6/17 21:15
 */

class KeyBind(
    val key: Key, val window: Window,
) {
    fun getKeyMode(): Int = GLFW.glfwGetKey(window.windowLocation, key.glfwKey)
    fun keyPress(): Boolean = GLFW.GLFW_PRESS == getKeyMode()
    fun keyRelease(): Boolean = GLFW.GLFW_RELEASE == getKeyMode()
}