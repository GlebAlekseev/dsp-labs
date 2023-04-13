package com.glebalekseevjk.common.ui.widget.data

import com.glebalekseevjk.common.ui.screen.repository
import kotlin.math.absoluteValue

class DiscreteOutputSignalLineChartData : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        val signalList = repository.resultDataState.value.outputReferencesValuesSignal
        val iterations = 10
        val step = (signalList.axisX[1] - signalList.axisX[0]).absoluteValue / iterations
        for (i in 0 until signalList.axisX.size) {
            var sum = 0.0
            repeat(iterations + 1) {
                axisX.add(signalList.axisX[i] + sum)
                axisY.add(signalList.axisY[i])
                sum += step
            }
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}