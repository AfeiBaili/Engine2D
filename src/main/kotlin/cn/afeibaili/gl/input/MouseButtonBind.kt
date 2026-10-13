package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window
import org.lwjgl.glfw.GLFW


/**
 * # 鼠标按键绑定类
 *
 * @author AfeiBaili
 * @version 2026/9/2 12:17
 */

class MouseButtonBind(val mouseButton: MouseButton, val window: Window) {
    fun getMouseButton() = GLFW.glfwGetMouseButton(window.pointer, mouseButton.glfwButton)
    fun buttonPressed() = GLFW.GLFW_PRESS == getMouseButton()
    fun buttonReleased() = GLFW.GLFW_RELEASE == getMouseButton()

    fun getIsPressAlt() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
    fun getIsPressCtrl() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_CONTROL) == GLFW.GLFW_PRESS
    fun getIsPressShift() = GLFW.glfwGetKey(window.pointer, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS

    private var lastIsPressed = false
    fun released(click: () -> Unit): Boolean {
        if (lastIsPressed && buttonReleased()) {
            click()
            lastIsPressed = false
            return true
        }
        lastIsPressed = buttonPressed()
        return false
    }
}