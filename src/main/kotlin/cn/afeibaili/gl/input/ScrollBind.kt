package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window


/**
 * # 鼠标滚轮绑定事件
 *
 * @author AfeiBaili
 * @version 2026/10/9 09:40
 */

class ScrollBind(val scroll: Scroll, val window: Window) {
    var xv = scroll.value
    var yv = scroll.value
    val value: Double get() = scroll.value

    fun down(action: () -> Unit) {
        while (yv > value) {
            yv -= value
            action()
        }
    }

    fun up(action: () -> Unit) {
        while (yv < value) {
            yv += value
            action()
        }
    }

    fun left(action: () -> Unit) {
        while (xv < value) {
            xv += value
            action()
        }
    }

    fun right(action: () -> Unit) {
        while (xv > value) {
            xv -= value
            action()
        }
    }
}