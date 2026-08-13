package cn.afeibaili.gl.render.layout


/**
 * # 容器布局接口
 *
 * @author AfeiBaili
 * @version 2026/8/10 12:42
 */

interface Containable<Layout : cn.afeibaili.gl.render.layout.Layout> {
    val items: MutableList<Layout>
}