package cn.afeibaili.gl.render.layout


/**
 * # 权重布局
 *
 * @author AfeiBaili
 * @version 2026/8/10 12:43
 */

interface WeightLayout : Layout {
    var weight: Float
}

/**
 * # 权重容器接口
 *
 * @author AfeiBaili
 * @version 2026/08/10 19:27
 */

interface WeightContainer : Containable<WeightLayout> {
    val setting: SettingWeight
    fun init()
}

/**
 * # 权重容器抽象类
 *
 * @author AfeiBaili
 * @version 2026/08/11 14:02
 */

abstract class AbstractWeightContainerLayout(override val setting: SettingWeight) :
    AbstractLayout(setting),
    WeightContainer {

    fun size(): Float {
        var size = 0f
        for (element in items) size += element.weight
        return size
    }

    operator fun WeightLayout.unaryPlus(): WeightLayout {
        return append()
    }

    fun WeightLayout.append(): WeightLayout {
        items.add(this)
        return this
    }
}

/**
 * # 基于行权重的容器
 *
 * @author AfeiBaili
 * @version 2026/8/10 12:23
 */

class RowWeight(
    override val setting: SettingWeight = SettingWeight(),
    override var container: Layout,
    val builder: RowWeight.() -> Unit,
) : AbstractWeightContainerLayout(setting) {
    override val items: MutableList<WeightLayout> = mutableListOf()
    override fun init() {
        builder.invoke(this)
        val rowHeight: Float = container.height / size()
        items.forEach { layout ->
            layout.height = rowHeight * layout.weight
            layout.width = container.width
        }
    }
}

/**
 * # 基于列权重的容器
 *
 * @author AfeiBaili
 * @version 2026/8/10 12:23
 */

class ColumnWeight(
    override val setting: SettingWeight = SettingWeight(),
    override var container: Layout,
    val builder: ColumnWeight.() -> Unit,
) : AbstractWeightContainerLayout(setting) {
    override val items: MutableList<WeightLayout> = mutableListOf()
    override fun init() {
        builder.invoke(this)
        val columnWidth: Float = container.width / size()
        items.forEach { layout ->
            layout.width = columnWidth * layout.weight
            layout.height = container.height
        }

    }
}