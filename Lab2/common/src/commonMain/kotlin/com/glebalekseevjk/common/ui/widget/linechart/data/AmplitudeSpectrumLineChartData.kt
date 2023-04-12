package com.glebalekseevjk.common.ui.widget.linechart.data

import com.glebalekseevjk.common.Repository
import ui.screen.repository

internal object AmplitudeSpectrum1 : LineChartData {
    override var axisX: MutableList<Double> = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val spectrumWidth = repository.getSpectrumWidth(Repository.VARIABLE_DELTA_CASE_1)
        axisX = (1..spectrumWidth.toInt() step 2000).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(repository.getAmplitudeSpectrum(n))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object AmplitudeSpectrum2 : LineChartData {
    override var axisX: MutableList<Double> = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val spectrumWidth = repository.getSpectrumWidth(Repository.VARIABLE_DELTA_CASE_2)
        axisX = (1..spectrumWidth.toInt() step 2000).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(repository.getAmplitudeSpectrum(n))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object AmplitudeSpectrum3 : LineChartData {
    override var axisX: MutableList<Double> = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val spectrumWidth = repository.getSpectrumWidth(Repository.VARIABLE_DELTA_CASE_3)
        axisX = (1..spectrumWidth.toInt() step 2000).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(repository.getAmplitudeSpectrum(n))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object AmplitudeSpectrum4 : LineChartData {
    override var axisX: MutableList<Double> = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val spectrumWidth = repository.getSpectrumWidth(Repository.VARIABLE_DELTA_CASE_4)
        axisX = (1..spectrumWidth.toInt() step 2000).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(repository.getAmplitudeSpectrum(n))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object AmplitudeSpectrum5 : LineChartData {
    override var axisX: MutableList<Double> = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val spectrumWidth = repository.getSpectrumWidth(Repository.VARIABLE_DELTA_CASE_5)
        axisX = (1..spectrumWidth.toInt() step 2000).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(repository.getAmplitudeSpectrum(n))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}