package com.glebalekseevjk.common.ui.widget.linechart.data

import ui.screen.castdfRepository
import kotlin.math.absoluteValue

object ErrorQuantizedSignalData : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val discreteSignal = castdfRepository.getGeneratedDiscreteSignal()
        val quantizedSignal = castdfRepository.getQuantizedSignal(discreteSignal)
        val errorList = castdfRepository.getQuantizationErrors(discreteSignal, quantizedSignal.first)
        val iterations = 10
        val step = (errorList[1].first - errorList[0].first).absoluteValue / iterations
        for (i in 0 until errorList.size) {
            var sum = 0.0
            repeat(iterations + 1) {
                axisX.add(errorList[i].first + sum)
                axisY.add(errorList[i].second)
                sum += step
            }
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}