import cn.afeibaili.gl.render.layout.RowWeight
import cn.afeibaili.gl.render.layout.SettingWeight
import cn.afeibaili.gl.render.layout.WeightLayout
import kotlin.test.Test

/**
 *
 *
 * @author AfeiBaili
 * @version 2026/8/10 21:46
 */

class TestLayout {
    @Test
    fun testLayout() {
        RowWeight(SettingWeight()) {
            +WeightLayout()
        }
    }
}