package com.glebalekseevjk.common

import kotlin.math.*

/**
 * SPECTRAL ANALYSIS OF PERIODIC SIGNALS
 */

class SAPSRepository {
    fun getAveragePower() = (2 * VARIABLE_EMax * VARIABLE_EMax * VARIABLE_tu) / VARIABLE_T

    fun getA0Factor() = 0.0

    fun getAnFactor(n: Int): Double {
        val numerator: Double = 2 * VARIABLE_EMax * (sin(n * VARIABLE_W * VARIABLE_tu) +
                sin(n * VARIABLE_W * (VARIABLE_T / 2)) - sin(n * VARIABLE_W * (VARIABLE_T / 2 + VARIABLE_tu)))
        val divider: Double = (VARIABLE_T * n * VARIABLE_W)
        if (divider == 0.0) return 0.0
        return numerator / divider
    }

    fun getBnFactor(n: Int): Double {
        val numerator: Double = 2 * VARIABLE_EMax * (1 - cos(n * VARIABLE_W * VARIABLE_tu) +
                cos(n * VARIABLE_W * (VARIABLE_T / 2 + VARIABLE_tu)) - cos(n * VARIABLE_W * (VARIABLE_T / 2)))
        val divider: Double = (VARIABLE_T * n * VARIABLE_W)
        if (divider == 0.0) return 0.0
        return numerator / divider
    }

    fun getAmplitude(n: Int): Double = sqrt(getAnFactor(n).pow(2.0) + getBnFactor(n).pow(2.0))

    fun getPhase(n: Int): Double {
        val anFactor = getAnFactor(n)
        if (anFactor == 0.0) return 0.0
        val res = getBnFactor(n) / anFactor
        return atan(if (res == -0.0) 0.0 else res)
    }

    fun getMaxNumberOfHarmonics(delta: Double): Int {
        val pc = getAveragePower()
        val left = (getA0Factor() / 2.0).pow(2.0)
        var right = 0.0
        var n = 1
        while (true) {
            if (n == 1000000) return -1
            right += getAmplitude(n).pow(2.0)
            val pk = left + 0.5 * right
            if ((pc - pk) / pc <= delta) return n - 1
            n++
        }
    }

    fun getAmplitude(n: Int, t: Double): Double {
        var sum = getA0Factor() / 2.0
        for (i in 1..n) {
            sum += getAmplitude(i) * cos(i * VARIABLE_W * t - getPhase(i))
        }
        return sum
    }

    companion object {
        // Вариант 2
        const val VARIABLE_EMax = 10 // В
        const val VARIABLE_tu = 0.000112 // 112*10^-6 секунд
        const val VARIABLE_T = 0.000448 // 448*10^-6
        const val VARIABLE_W = (2 * Math.PI) / VARIABLE_T // 448*10^-6

        const val VARIABLE_DELTA_CASE_1 = 0.1
        const val VARIABLE_DELTA_CASE_2 = 0.05
        const val VARIABLE_DELTA_CASE_3 = 0.02
        const val VARIABLE_DELTA_CASE_4 = 0.01
        const val VARIABLE_DELTA_CASE_5 = 0.001
    }
}