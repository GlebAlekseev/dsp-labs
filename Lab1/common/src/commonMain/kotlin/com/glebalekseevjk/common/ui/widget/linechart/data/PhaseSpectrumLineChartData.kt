package com.glebalekseevjk.common.ui.widget.linechart.data

import com.glebalekseevjk.common.SAPSRepository
import ui.screen.sapsRepository

internal object PhaseSpectrum1 : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val maxCountHarmonics = sapsRepository.getMaxNumberOfHarmonics(SAPSRepository.VARIABLE_DELTA_CASE_1)
        axisX = (1..maxCountHarmonics).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(sapsRepository.getPhase(n.toInt()))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object PhaseSpectrum2 : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val maxCountHarmonics = sapsRepository.getMaxNumberOfHarmonics(SAPSRepository.VARIABLE_DELTA_CASE_2)
        axisX = (1..maxCountHarmonics).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(sapsRepository.getPhase(n.toInt()))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object PhaseSpectrum3 : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val maxCountHarmonics = sapsRepository.getMaxNumberOfHarmonics(SAPSRepository.VARIABLE_DELTA_CASE_3)
        axisX = (1..maxCountHarmonics).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(sapsRepository.getPhase(n.toInt()))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object PhaseSpectrum4 : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val maxCountHarmonics = sapsRepository.getMaxNumberOfHarmonics(SAPSRepository.VARIABLE_DELTA_CASE_4)
        axisX = (1..maxCountHarmonics).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(sapsRepository.getPhase(n.toInt()))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}

internal object PhaseSpectrum5 : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val maxCountHarmonics = sapsRepository.getMaxNumberOfHarmonics(SAPSRepository.VARIABLE_DELTA_CASE_5)
        axisX = (1..maxCountHarmonics).toList().map { it.toDouble() }.toMutableList()

        for (n in axisX) {
            axisY.add(sapsRepository.getPhase(n.toInt()))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}
