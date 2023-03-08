package com.glebalekseevjk.common.ui.widget.linechart.data

import ui.screen.castdfRepository
import kotlin.math.absoluteValue

object QuantizedSignalData : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()
    val gradedValues: List<Double>

    init {
        val (signalList, _gradedValues) = castdfRepository.getQuantizedSignal(castdfRepository.getGeneratedDiscreteSignal())
        gradedValues = _gradedValues
        val iterations = 10
        val step = (signalList[1].first - signalList[0].first).absoluteValue / iterations
        for (i in 0 until signalList.size) {
            var sum = 0.0
            repeat(iterations + 1) {
                axisX.add(signalList[i].first + sum)
                axisY.add(signalList[i].second)
                sum += step
            }
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}