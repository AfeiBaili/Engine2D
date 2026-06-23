package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # Key按下（短按）
 *
 * @author AfeiBaili
 * @version 2026/6/23 20:13
 */

class KeyTap(key: Key, window: Window, callback: () -> Unit) : KeyListener(key, window, callback) {
    var isTriggered = false

    override fun action() {
        val mode: Int = getKeyMode()
        when (mode) {
            GLFW.GLFW_PRESS -> isAction = true
            GLFW.GLFW_RELEASE -> {
                isAction = false
                isTriggered = false
            }
        }
    }

    fun isKeyPressed(): Boolean {
        if (!isTriggered && isAction) {
            isTriggered = true
            return true
        }
        return false
    }

}