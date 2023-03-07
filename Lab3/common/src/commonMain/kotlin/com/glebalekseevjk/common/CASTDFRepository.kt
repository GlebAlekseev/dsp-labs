package com.glebalekseevjk.common

import compxclib.ComplexNumber
import kotlin.math.*

/**
 * Converting an analog signal to digital form
 */

class CASTDFRepository {

    fun getTotalEnergy(): Double =
        (VARIABLE_T.pow(3) * VARIABLE_a.pow(2)) / 3 +
                VARIABLE_T.pow(2) * VARIABLE_a * VARIABLE_b +
                VARIABLE_T * VARIABLE_b.pow(2)

    fun getSignalSpectralDensity(w: Double): ComplexNumber {
        var re = VARIABLE_a / w * (-1 / w + cos(w * VARIABLE_T) / w + VARIABLE_T * sin(w * VARIABLE_T))
        +(VARIABLE_b * sin(w * VARIABLE_T)) / VARIABLE_b

        var im = 1 / w * (-VARIABLE_b + VARIABLE_b * cos(w * VARIABLE_T) + VARIABLE_a * VARIABLE_T * cos(w * VARIABLE_T)
                - (VARIABLE_a * sin(w * VARIABLE_T)) / w)
        if (re.isNaN()) re = 0.0
        if (im.isNaN()) im = 0.0
        return ComplexNumber(re, im)
    }

    fun getConcentratedEnergy(wk: Double): Double {
        val res = ((2 * VARIABLE_T * VARIABLE_a.pow(2) * sin(VARIABLE_T * wk)) / wk.pow(2) +
                2 * VARIABLE_T * (3 * VARIABLE_b.pow(2) + 3 * VARIABLE_T * VARIABLE_a * VARIABLE_b + VARIABLE_T.pow(2) * VARIABLE_a.pow(
            2
        )) * si(
            VARIABLE_T * wk
        ) +
                (2 * (3 * VARIABLE_b.pow(2) + 3 * VARIABLE_T * VARIABLE_a * VARIABLE_b + VARIABLE_T.pow(2) * VARIABLE_a.pow(
                    2
                ))
                        * cos(VARIABLE_T * wk) - 6 * VARIABLE_b.pow(2) - 6 * VARIABLE_T * VARIABLE_a * VARIABLE_b - 3 * VARIABLE_T.pow(
                    2
                ) * VARIABLE_a.pow(2)) / wk +
                (2 * VARIABLE_a.pow(2) * cos(VARIABLE_T * wk) - 2 * VARIABLE_a.pow(2)) / wk.pow(3)) / (3 * Math.PI)
        if (res.isNaN()) return 0.0
        return res
    }

    fun getSpectrumWidth(): Double {
        val ec = getTotalEnergy()
        var wk = 0.0
        while (true) {
            val ek = getConcentratedEnergy(wk)
            if (VARIABLE_DELTA > ((ec - ek) / ec)) {
                return wk
            }
            wk += 100
            if (wk > 10.0.pow(9.0)) throw RuntimeException("wk > ${10.0.pow(9.0)}")
        }
    }

    fun getSpectrumWidthHz(): Double = getSpectrumWidth() / (Math.PI * 2)

    fun getSamplingFrequencyAnalogSignal(): Double = 2 * getSpectrumWidthHz()

    fun getRawSamplingStep(): Double = 1 / getSamplingFrequencyAnalogSignal()

    fun getNumberReadoutValues(): Int = (1 + (VARIABLE_T / getRawSamplingStep())).roundUpToEven()

    fun getFinalSamplingStep(): Double = VARIABLE_T / (getNumberReadoutValues() - 1)

    fun getGeneratedDiscreteSignal(): List<Pair<Double, Double>> {
        val valueN = getNumberReadoutValues()
        val finalSamplingStep = getFinalSamplingStep()

        val result = mutableListOf<Pair<Double, Double>>()
        for (i in 0 until valueN) {
            result.add(Pair(i * finalSamplingStep, sourceSignal(i * finalSamplingStep)))
        }
        return result
    }

    fun getQuantizedSignal(sourceSignal: List<Pair<Double, Double>>): Pair<List<Pair<Double, Double>>, List<Double>> {
        val maxS = sourceSignal.maxOf { it.second }
        val minS = sourceSignal.minOf { it.second }
        val quantizationStep = (maxS - minS) / (VARIABLE_M - 1)
        val quantizedSignal = sourceSignal.map {
            it.copy(
                second = obtainQuantizedValue(
                    it.second,
                    quantizationStep,
                    if (maxS.absoluteValue > minS.absoluteValue) minS else maxS
                )
            )
        }

        var sum = 0.0
        val list = buildList {
            repeat(16) {
                add(
                    obtainQuantizedValue(
                        minS + sum * (if (minS < 0) 1 else -1),
                        quantizationStep,
                        if (maxS.absoluteValue > minS.absoluteValue) minS else maxS
                    )
                )
                sum += quantizationStep
            }
        }
        return Pair(quantizedSignal, list)
    }

    private fun obtainQuantizedValue(value: Double, quantizationStep: Double, startValue: Double): Double {
        val progress = value - startValue
        val count = (progress / quantizationStep).roundToInt()
        val downValue = count * quantizationStep
        val result =
            if ((progress - downValue).absoluteValue > quantizationStep / 2) (if (downValue < 0) downValue - quantizationStep else downValue + quantizationStep) else downValue
        return result + startValue
    }

    fun getQuantizationErrors(
        sourceSignal: List<Pair<Double, Double>>,
        quantizedSignal: List<Pair<Double, Double>>
    ): List<Pair<Double, Double>> {
        val result = mutableListOf<Pair<Double, Double>>()
        for (i in sourceSignal.indices) {
            result.add(sourceSignal[i].copy(second = quantizedSignal[i].second - sourceSignal[i].second))
        }
        return result
    }

    fun getDiscreteFourierTransformCoefficients(quantizedSignal: List<Pair<Double, Double>>): List<ComplexNumber> {
        val valueN = quantizedSignal.size
        val result = mutableListOf<ComplexNumber>()
        for (n in 0 until valueN) {
            var sum = ComplexNumber(0, 0)
            for (k in 0 until valueN) {
                val valueRe = quantizedSignal[k].second * cos(2 * Math.PI * n * k / valueN)
                val valueIm = -quantizedSignal[k].second * sin(2 * Math.PI * n * k / valueN)
                sum += ComplexNumber(valueRe, valueIm)
            }
            result.add(sum / valueN)
        }
        return result
    }

    fun getRestoredDpfSignal(discreteFourierTransformCoefficients: List<ComplexNumber>, t: Double): Double {
        var sum = discreteFourierTransformCoefficients[0].re()
        for (i in 1 until discreteFourierTransformCoefficients.size / 2) {
            sum += 2 * discreteFourierTransformCoefficients[i].mag() * cos(2 * i * Math.PI * t / VARIABLE_T + discreteFourierTransformCoefficients[i].arg())
        }

        sum += discreteFourierTransformCoefficients[discreteFourierTransformCoefficients.size / 2].mag() *
                cos(2 * discreteFourierTransformCoefficients.size / 2 * Math.PI * t / VARIABLE_T + discreteFourierTransformCoefficients[discreteFourierTransformCoefficients.size / 2].arg())
        return sum
    }

    companion object {
        // Вариант 2
        const val VARIABLE_a = 1.0 // a, 1/c
        const val VARIABLE_b = -0.5 // В
        const val VARIABLE_T = 0.0000005 // с
        const val VARIABLE_M = 16 // кол-во ур-ней квантования
        const val VARIABLE_DELTA = 0.02
        fun sourceSignal(t: Double) = VARIABLE_a * t + VARIABLE_b

        fun si(w: Double): Double {
            if (w == 0.0) return 0.0
            val eps = 0.000000000000000001
            var term = w
            var sum = w
            for (n in 1..Int.MAX_VALUE) {
                val k: Long = 2L * n + 1L
                term *= -w * w * (k - 2) / (2 * k * k * n)
                sum += term
                if (term * term <= eps * eps) {
                    break
                }
            }
            return sum
        }

        fun Double.roundUpToEven(): Int {
            val integer = this.toInt()
            val decimal = this - integer
            when (integer % 2) {
                0 -> {
                    if (decimal == 0.0) return integer
                    return integer + 2
                }
                1 -> return integer + 1
                else -> return integer
            }
        }
    }
}