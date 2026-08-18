package cn.afeibaili.gl.render.layout.align

import cn.afeibaili.gl.render.layout.AbstractLayout
import cn.afeibaili.gl.render.layout.Component
import cn.afeibaili.gl.render.layout.Layout
import cn.afeibaili.gl.render.layout.Setting
import cn.afeibaili.gl.render.layout.align.AlignmentType.*


/**
 * # 抽象排列布局
 *
 * @author AfeiBaili
 * @version 2026/8/17 18:40
 */
abstract class AbstractAlignmentLayout : AbstractLayout() {
    private fun top(component: Component) {
        component.relativeY = 0f
    }

    private fun bottom(component: Component) {
        component.relativeY = component.container.height - component.height
    }

    private fun left(component: Component) {
        component.relativeX = 0f
    }

    private fun right(component: Component) {
        component.relativeX = component.container.width - component.width
    }

    private fun center(component: Component) {
        val parent: Layout = component.container
        component.relativeX = parent.width / 2 - component.width / 2
        component.relativeY = parent.height / 2 - component.height / 2
    }

    override fun update() {
        for (component in items) {
            when (component.align) {
                CENTER -> center(component)

                LEFT_TOP -> {
                    left(component)
                    top(component)
                }

                RIGHT_TOP -> {
                    right(component)
                    top(component)
                }

                LEFT_BOTTOM -> {
                    left(component)
                    bottom(component)
                }

                RIGHT_BOTTOM -> {
                    right(component)
                    bottom(component)
                }

                LEFT_CENTER -> {
                    center(component)
                    left(component)
                }

                RIGHT_CENTER -> {
                    center(component)
                    right(component)
                }

                TOP_CENTER -> {
                    center(component)
                    top(component)
                }

                BOTTOM_CENTER -> {
                    center(component)
                    bottom(component)
                }
            }
        }
        super.update()
    }
}

/**
 * # 排列布局
 *
 * @author AfeiBaili
 * @version 2026/8/17 18:45
 */

class AlignmentLayout(override var container: Layout) : AbstractAlignmentLayout()

fun Layout.block(setting: (Setting) -> Unit = {}, action: AlignmentLayout.() -> Unit): AlignmentLayout {
    val layout = AlignmentLayout(this)
    val set = Setting()
    setting(set)
    set.apply(layout)

    +layout
    action(layout)
    return layout
}