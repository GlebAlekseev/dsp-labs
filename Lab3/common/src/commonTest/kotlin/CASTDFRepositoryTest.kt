import com.glebalekseevjk.common.Repository.Companion.VARIABLE_T
import com.glebalekseevjk.common.Repository.Companion.roundUpToEven
import org.junit.Test

class Test1 {
    val castdfRepository = CASTDFRepository()

    @Test
    fun testRound() {
        println(10.1.roundUpToEven())
        println(10.5.roundUpToEven())
        println(10.9.roundUpToEven())
        println(11.9.roundUpToEven())
        println(12.0.roundUpToEven())
        println(12.1.roundUpToEven())
    }

    @Test
    fun testGetTotalEnergy(){
        val result = castdfRepository.getTotalEnergy()
        println(result)
    }

    @Test
    fun testGetSignalSpectralDensity(){
        for (i in 0..1800 step 1 ){
            val arg = i.toDouble()
            val res = castdfRepository.getSignalSpectralDensity(arg)
//            println(">>> arg=$arg res=$res res.mod=${res.mag()}")
        }
    }

    @Test
    fun testGetConcentratedEnergy(){
        for (i in 0..180 step 1 ){
            val arg = i.toDouble()
            val res = castdfRepository.getConcentratedEnergy(arg)
            println(">>> arg=$arg res=$res")
        }
    }

    @Test
    fun testGetSpectrumWidth(){
        val result = castdfRepository.getSpectrumWidth()
        val freqd = result / Math.PI
        println("wk=${result} freq_d=$freqd шаг дискр=${1/freqd} N=${(1+ VARIABLE_T/(1/freqd)).roundUpToEven()} T=${VARIABLE_T}")
    }

    @Test
    fun testGetSamplingFrequencyAnalogSignal(){
        val result = castdfRepository.getSamplingFrequencyAnalogSignal()
        println(result)
    }

    @Test
    fun testGetGeneratedDiscreteSignal(){
        val result = castdfRepository.getGeneratedDiscreteSignal()
        println(result)
    }

    @Test
    fun testGetQuantizedSignal(){
        val source = castdfRepository.getGeneratedDiscreteSignal()
        val result = castdfRepository.getQuantizedSignal(source)
        println(source)
        println(result)
    }

    @Test
    fun testGetQuantizationErrors(){
        val source = castdfRepository.getGeneratedDiscreteSignal()
        val quant = castdfRepository.getQuantizedSignal(source)
        val result = castdfRepository.getQuantizationErrors(source,quant.first)
        println(source)
        println(quant)
        println(result)
    }

    @Test
    fun testGetDiscreteFourierTransformCoefficients(){
        val source = castdfRepository.getGeneratedDiscreteSignal()
        val quant = castdfRepository.getQuantizedSignal(source)
        val result = castdfRepository.getDiscreteFourierTransformCoefficients(quant.first)
        println(source)
        println(quant)
        println(result.joinToString("\n"))
    }

    @Test
    fun testGetRestoredDpfSignal(){
        val source = castdfRepository.getGeneratedDiscreteSignal()
        val quant = castdfRepository.getQuantizedSignal(source)
        val coefs = castdfRepository.getDiscreteFourierTransformCoefficients(quant.first)
        var sum = 0.0
        while (sum < VARIABLE_T){
            val result = castdfRepository.getRestoredDpfSignal(coefs, sum)
            val sourceSignal = CASTDFRepository.sourceSignal(sum)
            println(">>> t=${sum} \n\trestored=\t\t$result \n\tsourceSignal=\t${sourceSignal}")
            sum += VARIABLE_T/1000
        }
    }
}