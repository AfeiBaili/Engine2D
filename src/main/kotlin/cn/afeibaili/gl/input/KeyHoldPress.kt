package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # Key按下（长按）
 *
 * @author AfeiBaili
 * @version 2026/6/23 20:13
 */

class KeyHoldPress(key: Key, window: Window, callback: () -> Unit) : KeyListener(key, window, callback) {
    /**
     * 需要持续检测
     */
    override fun action() {
        val mode: Int = getKeyMode()
        when (mode) {
            GLFW.GLFW_PRESS -> isAction = true
            GLFW.GLFW_RELEASE -> isAction = false
        }

        if (isAction) super.callback()
    }
}