package com.glebalekseevjk.common

import compxclib.ComplexNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.*


class Repository() {
    val resultDataState: StateFlow<ResultData>
        get() = _resultDataState
    private val _resultDataState: MutableStateFlow<ResultData> = MutableStateFlow(ResultData())
}

class ResultData {
    // 1) Рассчитать вспомогательные параметры АЧХ
    val delta1: Double = (10.0.pow(0.05 * A_P) - 1) / (10.0.pow(0.05 * A_P) + 1)
    val delta2: Double = 10.0.pow(-0.05 * A_Z)
    val delta: Double = min(delta1, delta2)

    // 2) Рассчитать ширину переходной полосы и середину переходной полосы для АЧХ ФНЧ:
    val fTransition = F_Z - F_C // Ширина переходной полосы
    val fMiddleTransition = F_C + fTransition / 2.0 // Середина переходной полосы

    // 3) Рассчитать порядок фильтра M, используя значение D-фактора по весовым функциям Кайзера:
    val a = -20 * log10(delta)
    val d = when {
        a <= 21 -> 0.9222
        else -> (a - 7.95) / 14.36
    } // D-фактор по весовым функциям Кайзера

    var m = ((F_D * d) / fTransition).roundUp().toInt() // Порядок фильтра M

    // Кол-во ответвлений N = M + 1
    val n: Int
        get() = m + 1

    // 4) Рассчитать параметр весовой функции Кайзера alpha
    val alpha = when {
        a >= 50.0 -> 0.1102 * (a - 8.7)
        21 < a && a < 50.0 -> 0.5842 * ((a - 21.0).pow(0.4)) + 0.07886 * (a - 21.0)
        else -> 0.0
    }

    // 5) Рассчитать коэффициенты Фурье alpha i, i=0,…,M/2 для полученных значений fc1
    fun alphaI(i: Int): Double = when {
        m % 2 == 0 && i == 0 -> 2 * fMiddleTransition / F_D
        m % 2 == 0 -> (1 / (Math.PI * i)) * sin(2 * Math.PI * i * fMiddleTransition / F_D)
        else -> (1 / (Math.PI * (i - 1 / 2.0))) * sin(2 * Math.PI * (i - 1 / 2.0) * fMiddleTransition / F_D)
    }

    // 6) Вычислить коэффициенты фильтра
    fun hi(i: Int): Double = when {
        i in 0 until m / 2 -> alphaI(m / 2 - i)
        i == m / 2 -> alphaI(0)
        else -> hi(m - i)
    }

    // 7) Рассчитать АЧХ фильтра и проверить ее соответствие заданным требованиям.
    // Нормировать и представить в логарифмическом масштабе
    private fun h(w: Double): ComplexNumber {
        var sumRe = 0.0
        var sumIm = 0.0
        for (i in 0..m) {
            sumRe += hi(i) * cos(i * w / (F_D))
            sumIm += hi(i) * -sin(i * w / (F_D))
        }
        return ComplexNumber(sumRe, sumIm)
    }

    init {
        // Валидация
        for (i in 0..2001) {
            var isGood = true
            val hList = List((F_D / 2.0).toInt() + 1) { w -> h(w.toDouble() * 2 * Math.PI).mag() }
            val max = hList.max()
            val normalizedHList = hList.map { it / max }

            for (w in 0..(F_D / 2.0).toInt() step 1) {
                when (w) {
                    in 0 until F_C.toInt() -> if ((normalizedHList[w] - 1).absoluteValue > delta1) {
                        isGood = false
                        continue
                    }
                    in F_Z.toInt()..(F_D / 2.0).toInt() -> if (normalizedHList[w] > delta2) {
                        isGood = false
                        continue
                    }
                }
            }
            if (isGood) break else m++
        }
    }

    val amplitudeFrequencyResponse: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            for (w in 0..(F_D / 2.0).toInt() step 1) {
                axis.axisX.add(w.toDouble() * 2 * Math.PI)
                axis.axisY.add(h(w.toDouble() * 2 * Math.PI).mag())
            }
            val max = axis.axisY.max()

            return Axis(axis.axisX.map { it }.toMutableList(), axis.axisY.map { it / max }.toMutableList())
        }

    val requiredCharacteristicsOfLPF: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            axis.axisX.add(0.0 * 2 * Math.PI)
            axis.axisY.add(1.0)

            axis.axisX.add(F_C * 2 * Math.PI)
            axis.axisY.add(1.0)

            axis.axisX.add(F_Z * 2 * Math.PI)
            axis.axisY.add(0.0)

            return axis
        }

    val lowerBandwidthLimit: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            axis.axisX.add(0.0 * 2 * Math.PI)
            axis.axisY.add(1 - delta1)

            axis.axisX.add(F_C * 2 * Math.PI)
            axis.axisY.add(1 - delta1)

            return axis
        }

    val upperBandwidthLimit: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            axis.axisX.add(0.0 * 2 * Math.PI)
            axis.axisY.add(1 + delta1)

            axis.axisX.add(F_C * 2 * Math.PI)
            axis.axisY.add(1 + delta1)

            return axis
        }

    val upperLimitOfDelayBand: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            axis.axisX.add(0.0 * 2 * Math.PI)
            axis.axisY.add(delta2)

            axis.axisX.add(F_D / 2 * 2 * Math.PI)
            axis.axisY.add(delta2)

            return axis
        }

    // ИХ
    val impulseResponse: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            for (i in 0..m) {
                axis.axisX.add(i.toDouble())
                axis.axisY.add(hi(i))
            }
            return axis
        }

    // ФЧХ
    val phaseFrequencyResponse: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            for (w in 0..(F_D / 2.0).toInt() step 1) {
                axis.axisX.add(w.toDouble() * 2 * Math.PI)
                val h = h(w.toDouble() * 2 * Math.PI)
                axis.axisY.add(atan(h.im() / h.re()))
            }
            return axis
        }

    // 12. Исходный тестовый сигнал представляет собой линейную комбинацию
    // гармонических составляющих сигнала
    private fun sourceSignal(t: Double) =
        cos(2 * Math.PI * t * F_1) + cos(2 * Math.PI * t * F_2) + cos(2 * Math.PI * t * F_3) +
                cos(2 * Math.PI * t * F_4) + cos(2 * Math.PI * t * F_5)

    val sourceSignal: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            val step = T / 100.0
            for (i in 0..100) {
                axis.axisX.add(i * step)
                axis.axisY.add(sourceSignal(i * step))
            }
            return axis
        }


    val rawSamplingStep: Double = 1 / (F_D)

    val numberReadoutValues: Int = 1 + (T / rawSamplingStep).roundUpToEven()

    val finalSamplingStep: Double = T / (numberReadoutValues - 1.0)

    val discreteSignal: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()

            for (i in 0 until numberReadoutValues) {
                axis.axisX.add(i * finalSamplingStep)
                axis.axisY.add(sourceSignal(i * finalSamplingStep))
            }
            return axis
        }

    private fun obtainQuantizedValue(value: Double, quantizationStep: Double, startValue: Double): Double {
        val progress = value - startValue
        val count = (progress / quantizationStep).roundToInt()
        val downValue = count * quantizationStep
        val result =
            if ((progress - downValue).absoluteValue > quantizationStep / 2) (if (downValue < 0) downValue - quantizationStep else downValue + quantizationStep) else downValue
        return result + startValue
    }

    private val maxYDiscreteSignal: Double = discreteSignal.axisY.max()

    private val minYDiscreteSignal: Double = discreteSignal.axisY.min()

    val quantizationStep = (maxYDiscreteSignal - minYDiscreteSignal) / (M - 1)

    val digitalSignal: Pair<Axis<Double, Double>, List<Double>>
        get() {
            val axis = Axis<Double, Double>()
            val quantizedSignal = discreteSignal.axisY.map {
                obtainQuantizedValue(
                    it,
                    quantizationStep,
                    if (maxYDiscreteSignal > minYDiscreteSignal) minYDiscreteSignal else maxYDiscreteSignal
                )
            }.toMutableList()

            var sum = 0.0
            val list = buildList {
                repeat(64) {
                    add(
                        obtainQuantizedValue(
                            minYDiscreteSignal + sum * (if (minYDiscreteSignal < 0) 1 else -1),
                            quantizationStep,
                            if (maxYDiscreteSignal.absoluteValue > minYDiscreteSignal.absoluteValue) minYDiscreteSignal else maxYDiscreteSignal
                        )
                    )
                    sum += quantizationStep
                }
            }
            return Pair(Axis(discreteSignal.axisX, quantizedSignal), list)
        }

    private fun outputAnalyticalSignal(t: Double): Double = h(F_1 * 2 * Math.PI).mag() * sin(F_1 * 2 * Math.PI * t) +
            h(F_2 * 2 * Math.PI).mag() * sin(F_2 * 2 * Math.PI * t) +
            h(F_3 * 2 * Math.PI).mag() * sin(F_3 * 2 * Math.PI * t) +
            h(F_4 * 2 * Math.PI).mag() * sin(F_4 * 2 * Math.PI * t) +
            h(F_5 * 2 * Math.PI).mag() * sin(F_5 * 2 * Math.PI * t)

    val outputAnalyticalSignal: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            val step = T / 500.0
            for (i in 0..500) {
                axis.axisX.add(i * step)
                axis.axisY.add(outputAnalyticalSignal(i * step))
            }
            return axis
        }

    private fun outputReferencesValuesSignal(i: Int): Double = outputAnalyticalSignal(i * finalSamplingStep)

    val outputReferencesValuesSignal: Axis<Double, Double>
        get() {
            val axis = Axis<Double, Double>()
            for (i in 0 until numberReadoutValues) {
                axis.axisX.add(i.toDouble())
                axis.axisY.add(outputReferencesValuesSignal(i))
            }
            return axis
        }

    companion object {
        const val F_C = 5.0 // fс - граница полосы пропускания [Гц] рад/c
        const val F_Z = 10.0 // fз - граница полосы задерживания [Гц]
        const val A_P = 1.0 // Aп - неравномерность в полосе пропускания [дБ]
        const val A_Z = 30.0 // Aз - неравномерность в полосе задерживания [дБ]
        const val F_D = 128.0 // fд - частота дискретизации [Гц]

        // частоты гармонических составляющих тестового сигнала
        const val F_1 = 1.0   // f1 [Гц]
        const val F_2 = 5.0  // f2 [Гц]
        const val F_3 = 7.0  // f3 [Гц]
        const val F_4 = 10.0  // f4 [Гц]
        const val F_5 = 40.0 // f5 [Гц]
        const val T = 1 // 1 sec
        const val M = 64 // ур-ни квантования
    }
}

data class Axis<T1, T2>(
    val axisX: MutableList<T1> = mutableListOf(),
    val axisY: MutableList<T2> = mutableListOf(),
)