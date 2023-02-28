import com.glebalekseevjk.common.SAPSRepository
import com.glebalekseevjk.common.SAPSRepository.Companion.VARIABLE_EMax
import com.glebalekseevjk.common.SAPSRepository.Companion.VARIABLE_T
import com.glebalekseevjk.common.SAPSRepository.Companion.VARIABLE_tu
import dev.benedikt.math.bezier.spline.DoubleBezierSpline
import dev.benedikt.math.bezier.vector.Vector2D
import org.junit.Assert
import org.junit.Test

class Test1 {
    private val sapsRepository = SAPSRepository()

    @Test
    fun testGetAveragePower() { // +
        val actual = sapsRepository.getAveragePower()
        val expected = 5.0
        println("(2*$VARIABLE_EMax*$VARIABLE_EMax*$VARIABLE_tu)/$VARIABLE_T")
        println("expected=${expected} actual=${actual}")
        Assert.assertEquals(actual, expected, 50.0)
    }

    @Test
    fun testGetA0Factor() { // +
        val actual = sapsRepository.getA0Factor()
        val expected = 0.0
        println("expected=${expected} actual=${actual}")
        Assert.assertEquals(actual, expected, 0.000000000001)
    }

    @Test
    fun testGetAnFactor() {
        for (i in 0..100) {
            val actual = sapsRepository.getAnFactor(i)
            println("n=${i}\t AnFactor=${actual}")
        }
    }

    @Test
    fun testGetBnFactor() {
        for (i in 0..100) {
            val actual = sapsRepository.getBnFactor(i)
            println("n=${i}\t BnFactor=${actual}")
        }
    }

    @Test
    fun testGetAmplitude() {
        for (i in 0..100) {
            val actual = sapsRepository.getAmplitude(i)
            println("n=${i}\t Amplitude=${actual}")
        }
    }

    @Test
    fun testGetAmplitude2() {
        listOf(4, 8, 20, 40, 404).forEach {
            val actual = sapsRepository.getAmplitude(it)
            println("n=${it}\t Amplitude=${actual}")
        }
    }

    @Test
    fun testGetPhase() {
        for (i in 0..100) {
            val actual = sapsRepository.getPhase(i)
            println("n=${i}\t Phase=${actual}")
        }
    }

    @Test
    fun testGetPhase2() {
        listOf(4, 8, 20, 40, 404).forEach {
            val actual = sapsRepository.getPhase(it)
            println("n=${it}\t Phase=${actual}")
        }
    }

    private val deltaCases = mapOf(
        1 to SAPSRepository.VARIABLE_DELTA_CASE_1,
        2 to SAPSRepository.VARIABLE_DELTA_CASE_2,
        3 to SAPSRepository.VARIABLE_DELTA_CASE_3,
        4 to SAPSRepository.VARIABLE_DELTA_CASE_4,
        5 to SAPSRepository.VARIABLE_DELTA_CASE_5
    )

    @Test
    fun testGetMaxNumberOfHarmonics() {
        for ((key, value) in deltaCases) {
            val actual = sapsRepository.getMaxNumberOfHarmonics(value)
            println("delta=${value}\t MaxN=${actual}")
        }
    }

    @Test
    fun testGetAmplitudeNT() {
        val nArray = listOf(4, 8, 20, 40, 404)
        var timer = 0.0
        repeat(448) {
            timer += 0.000001
            for (n in nArray) {
                val actual = sapsRepository.getAmplitude(n, timer)
                println("n=${n}\ttimer=$timer\t amplitude=${actual}")
            }
        }
    }

    @Test
    fun testSpline1() {
        val spline = DoubleBezierSpline<Vector2D>(true)

        spline.addKnots(
            Vector2D(60.0, 60.0),
            Vector2D(700.0, 240.0),
            Vector2D(600.0, 100.0),
            Vector2D(330.0, 390.0)
        )

        spline.compute()

        var start = 0.0
        repeat(100) {
            val res = spline.getCoordinatesAt(start)
            println(res)
            start += 0.01
        }
    }
}