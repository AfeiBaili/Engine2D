package cn.afeibaili.gl.input

import cn.afeibaili.gl.Window


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

    val value: Double get() = scroll.value

    fun down(action: () -> Unit) {
        while (dv >= value) {
            dv -= value
            action()
        }
    }

    fun up(action: () -> Unit) {
        while (uv >= value) {
            uv -= value
            action()
        }
    }

    fun left(action: () -> Unit) {
        while (lv >= value) {
            lv -= value
            action()
        }
    }

    fun right(action: () -> Unit) {
        while (rv >= value) {
            rv -= value
            action()
        }
    }
}