package cn.afeibaili.gl.font


/**
 * # 文本列表
 *
 * @author AfeiBaili
 * @version 2026/8/1 01:47
 */

class TextMap {
    val list = mutableMapOf<String, Text>()

    fun size() = list.values.sumOf { it.string.length }

    fun put(text: Text) {
        list.put(text.key, text)
    }

    fun forEach(func: (Text) -> Unit) {
        list.values.forEach(func)
    }

    fun forEachIndexed(func: (Int, Text) -> Unit) {
        list.values.forEachIndexed { index, text -> func(index, text) }
    }

    fun clear() {
        list.clear()
    }

    fun isEmpty() = list.isEmpty()
}