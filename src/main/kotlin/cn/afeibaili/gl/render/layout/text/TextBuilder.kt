package cn.afeibaili.gl.render.layout.text


class TextUpdaterBuilder {
    val updater = TextUpdater()
    operator fun Text.unaryPlus() {
        append(this)
    }

    fun append(text: Text) {
        val t: Text? = updater[text.key]
        if (t != null) error("已存在文本key: ${text.key}")
        updater.put(text.key, text)
    }
}

fun buildTextUpdater(action: TextUpdaterBuilder.() -> Unit): TextUpdater {
    val builder = TextUpdaterBuilder()
    action(builder)
    return builder.updater
}