package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # 鼠标滚轮绑定事件
 *
 * @author AfeiBaili
 * @version 2026/10/9 09:40
 */

class ScrollBind(val scroll: Scroll, val window: Window) {
    var dv = 0.0
    var uv = 0.0
    var lv = 0.0
    var rv = 0.0

    fun getIsPressAlt() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
    fun getIsPressCtrl() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
    fun getIsPressShift() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS

    val value: Double get() = scroll.value
    fun verifyPressModifierKey(): Boolean {
        return scroll.alt == getIsPressAlt() && scroll.ctrl == getIsPressCtrl() && scroll.shift == getIsPressShift()
    }

    fun down(action: () -> Unit) {
        if (!verifyPressModifierKey()) return
        while (dv >= value) {
            dv -= value
            action()
        }
    }

    fun up(action: () -> Unit) {
        if (!verifyPressModifierKey()) return
        while (uv >= value) {
            uv -= value
            action()
        }
    }

    fun left(action: () -> Unit) {
        if (!verifyPressModifierKey()) return
        while (lv >= value) {
            lv -= value
            action()
        }
    }

    fun right(action: () -> Unit) {
        if (!verifyPressModifierKey()) return
        while (rv >= value) {
            rv -= value
            action()
        }
    }
}