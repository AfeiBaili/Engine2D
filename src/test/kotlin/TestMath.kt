import kotlin.math.exp
import kotlin.test.Test

/**
 *
 *
 * @author AfeiBaili
 * @version 2026/9/2 22:50
 */

class TestMath {
    @Test
    fun test01() {
        for (i in 3 - 1 downTo 0) {
            println(computeLayerLight(i, 3))
        }
    }


    fun computeLayerLight(layerIndex: Int, size: Int): Float {
        return 1f - layerIndex / size.toFloat() / 2
    }

    @Test
    fun testExp() {
        val d: Double = 2 - 1 * (1f - exp(-0.2))
        println(d)
    }
}