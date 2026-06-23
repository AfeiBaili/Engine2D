package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # 按键回调
 *
 * @author AfeiBaili
 * @version 2026/6/17 21:15
 */

abstract class KeyListener(val key: Key, val window: Window, val callback: () -> Unit) {
    var isAction = false

    operator fun invoke() {
        action()
    }

    fun getKeyMode(): Int = GLFW.glfwGetKey(window.windowLocation, key.glfwKey)

    abstract fun action()
}