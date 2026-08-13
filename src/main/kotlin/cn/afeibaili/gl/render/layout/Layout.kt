package cn.afeibaili.gl.render.layout


/**
 * #  布局接口
 *
 * @author AfeiBaili
 * @version 2026/8/11 13:51
 */

interface Layout {
    val setting: Setting
    var x: Float
    var y: Float
    var offsetX: Float
    var offsetY: Float
    var width: Float
    var height: Float
    var container: Layout
}