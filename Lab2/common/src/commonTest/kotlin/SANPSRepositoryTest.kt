import com.glebalekseevjk.common.SANPSRepository
import org.junit.Test
import kotlin.math.absoluteValue

class Test1 {
    val sanpsRepository = SANPSRepository()

    @Test
    fun testGetSignalSpectralDensity() { // +
        var sum = 0.0
        for (i in 0..100) {
            val actual = sanpsRepository.getSignalSpectralDensity(sum)
            println("sum = $sum actual=${actual}")
            sum += i / 100.0

        }
    }

    @Test
    fun testGetAmplitudeSpectrum() { // +
        var sum = 0.0
        for (i in 0..10000) {
            val actual = sanpsRepository.getAmplitudeSpectrum(sum)
            println("sum = $sum actual=${actual}")
            sum += i / 100.0

        }
    }

    @Test
    fun testGetPhaseSpectrum() { // +
        var sum = 0.0
        for (i in 0..1000) {
            val actual = sanpsRepository.getPhaseSpectrum(sum)
            println("sum = $sum actual=${actual}")
            sum += 10

        }
    }

    @Test
    fun testGetTotalEnergy() { // +
        val actual = sanpsRepository.getTotalEnergy()
        println("actual=$actual")
    }

    @Test
    fun testGetConcentratedEnergy() { // +
        var sum = 0.0
        for (i in 0..1000) {
            val actual = sanpsRepository.getConcentratedEnergy(sum)
            println("wk = $sum actual=${actual}")
            sum += 1000
        }
    }


    @Test
    fun testGetSignalSpectralDensitySt() { // +
        var timer = 0.0
        val tu = 112
        repeat(tu * 2) {
            timer += 0.000001
            val actual = sanpsRepository.getSignalSpectralDensitySt(340448.0, timer)
            println("w=340448\ttimer=$timer\t actual=${actual} ${if (actual.absoluteValue > 10) "-----------------------BAD" else ""}")
        }
    }

    @Test
    fun testGetSpectrumWidth() { // +
        sanpsRepository.getSpectrumWidth(SANPSRepository.VARIABLE_DELTA_CASE_5)
    }

    @Test
    fun testSi() { // +
        var sum = 0.0
        for (i in 0..1000) {
            val actual = SANPSRepository.si(sum * SANPSRepository.VARIABLE_tu)
            println("wk = $sum sum*SANPSRepository.VARIABLE_t=${sum * SANPSRepository.VARIABLE_tu} actual=${actual}")
            sum += 1000
        }
    }

    @Test
    fun testSi1() { // +
//        var sum = 27.44
//        val actual = SANPSRepository.si(sum)
//        println("wk1 = ${sum} actual=${actual}")
//        var sum2 = 45.472
//        val actual2 = SANPSRepository.si(sum2)
//        println("wk2 = ${sum2} actual2=${actual2}")
//        var sum2 = 87500*SANPSRepository.VARIABLE_tu
//        val actual2 = SANPSRepository.si(sum2)
//        println("wk2 = ${sum2} actual2=${actual2}")
        var sum2 = 0.0
        val actual2 = SANPSRepository.si(sum2)
        println("wk2 = ${sum2} actual2=${actual2}")
    }
}