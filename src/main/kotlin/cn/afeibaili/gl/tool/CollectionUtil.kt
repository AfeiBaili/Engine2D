package cn.afeibaili.gl.tool

inline fun <reified Key, Value> Map<Key, List<Value>>.putElementOrCreateList(key: Key, value: Value) {
    this as MutableMap<Key, List<Value>>
    val v: List<Value>? = this[key]
    if (v == null) this[key] = mutableListOf(value)
    else {
        val list: MutableList<Value> = this[key] as MutableList<Value>
        list.add(value)
    }
}