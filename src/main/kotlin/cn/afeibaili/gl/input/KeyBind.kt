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
    fun keyPressed(): Boolean = GLFW.GLFW_PRESS == getKeyMode()
    fun keyReleased(): Boolean = GLFW.GLFW_RELEASE == getKeyMode()


    private var lastIsPressed = false
    fun released(click: () -> Unit): Boolean {
        if (lastIsPressed && keyReleased()) {
            click()
            lastIsPressed = false
            return true
        }
        lastIsPressed = keyPressed()
        return false
    }
}