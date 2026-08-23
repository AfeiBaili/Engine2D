package cn.afeibaili.gl.font


/**
 * # 字符类
 *
 *  uv结构
 *  * 索引0为起始x点
 *  * 索引1为起始y点
 *  * 索引2为终止x点
 *  * 索引3为终止y点
 *
 * @author AfeiBaili
 * @version 2026/7/30 19:20
 */

class Character(
    val char: Char,
    val uv: FloatArray,
    val width: Float,
    val height: Float,
    val bearingX: Float,
    val bearingTop: Float,
    val advance: Float,
)