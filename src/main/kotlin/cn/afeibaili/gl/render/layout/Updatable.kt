package cn.afeibaili.gl.render.layout


/**
 * # 可更新的
 *
 * @author AfeiBaili
 * @version 2026/10/8 14:15
 */

interface Updatable<ValueType> {
    val map: MutableMap<String, ValueType>
}