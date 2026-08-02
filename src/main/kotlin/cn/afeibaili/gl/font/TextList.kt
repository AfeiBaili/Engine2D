package cn.afeibaili.gl.font


/**
 * # 文本列表
 *
 * @author AfeiBaili
 * @version 2026/8/1 01:47
 */

class TextList {
    val list = ArrayList<Text>()

    fun size() = list.sumOf { it.string.length }

    fun add(text: Text) {
        list.add(text)
    }

    fun forEach(func: (Text) -> Unit) {
        list.forEach(func)
    }

    fun clear() {
        list.clear()
    }

    fun isEmpty() = list.isEmpty()
}