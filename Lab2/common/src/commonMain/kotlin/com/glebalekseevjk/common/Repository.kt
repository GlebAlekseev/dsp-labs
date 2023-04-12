package com.glebalekseevjk.common

import compxclib.ComplexNumber
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * SPECTRAL ANALYSIS OF NON-PERIODIC SIGNALS
 */

class Repository {
    fun getSignalSpectralDensity(w: Double): ComplexNumber {
        val re = (VARIABLE_EMax / w) * (sin(w * VARIABLE_tu))
        val im = (VARIABLE_EMax / w) * (cos(w * VARIABLE_tu) - 1)
        return ComplexNumber(re, im)
    }

    fun getAmplitudeSpectrum(w: Double): Double {
        val signalSpectralDensity = getSignalSpectralDensity(w)
        val mod = signalSpectralDensity.mag()
        return if (mod.isNaN()) 0.0 else mod
    }

    fun getPhaseSpectrum(w: Double): Double {
        val signalSpectralDensity = getSignalSpectralDensity(w)
        return atan(signalSpectralDensity.im() / signalSpectralDensity.re())
    }

    fun getTotalEnergy(): Double = VARIABLE_EMax * VARIABLE_EMax * VARIABLE_tu

    fun getConcentratedEnergy(wk: Double): Double {
        val res = ((2.0 * VARIABLE_EMax * VARIABLE_EMax) / Math.PI) *
                ((-1 / wk) + cos(wk * VARIABLE_tu) / wk + VARIABLE_tu * si(wk * VARIABLE_tu))
        if (res.isNaN()) return 0.0
        return res
    }

    fun getSpectrumWidth(delta: Double): Double {
        val ec = getTotalEnergy()
        var wk = 0.0
        while (true) {
            val ek = getConcentratedEnergy(wk)
            if (delta > ((ec - ek) / ec)) {
                return wk
            }
            wk += 1
            if (wk > 10.0.pow(7.0)) throw RuntimeException("wk > ${10.0.pow(7.0)}")
        }
    }

    fun getSignalSpectralDensitySt(w: Double, t: Double): Double {
        if (t == 0.0 || w == 0.0) return 0.0
        return VARIABLE_EMax * (si(w * (t + VARIABLE_tu)) + si(w * (VARIABLE_tu - t))) / (2 * Math.PI) -
                VARIABLE_EMax * (si(w * (t + VARIABLE_tu)) - si(w * (VARIABLE_tu - t))) / (2 * Math.PI) +
                VARIABLE_EMax * si(t * w) / Math.PI
    }

    companion object {
        // Вариант 2
        const val VARIABLE_EMax = 10 // В
        const val VARIABLE_tu = 0.000112 // 112*10^-6 секунд
        const val VARIABLE_DELTA_CASE_1 = 0.1
        const val VARIABLE_DELTA_CASE_2 = 0.05
        const val VARIABLE_DELTA_CASE_3 = 0.02
        const val VARIABLE_DELTA_CASE_4 = 0.01
        const val VARIABLE_DELTA_CASE_5 = 0.001

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
    }
}