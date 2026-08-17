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

    }

    private fun bottom(component: Component) {

    }

    private fun left(component: Component) {

    }

    private fun right(component: Component) {

    }

    private fun center(component: Component) {

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
                    left(component)
                    center(component)
                }

                RIGHT_CENTER -> {
                    right(component)
                    center(component)
                }

                TOP_CENTER -> {
                    top(component)
                    center(component)
                }

                BOTTOM_CENTER -> {
                    bottom(component)
                    center(component)
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

fun Layout.block(setting: Setting = Setting(), action: Layout.() -> Unit): AlignmentLayout {
    val layout = AlignmentLayout(this)
    setting.apply(layout)
    +layout
    action(layout)
    return layout
}