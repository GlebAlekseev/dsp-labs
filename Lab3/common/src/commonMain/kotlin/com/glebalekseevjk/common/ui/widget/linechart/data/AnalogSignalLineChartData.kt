package com.glebalekseevjk.common.ui.widget.linechart.data

internal object AnalogSignalData : LineChartData {
    override var axisX = mutableListOf<Double>()
    override var axisY = mutableListOf<Double>()

    init {
        var time = 0.0
        while (time <= CASTDFRepository.VARIABLE_T) {
            axisX.add(time)
            time += CASTDFRepository.VARIABLE_T / 100
        }
        for (t in axisX) {
            axisY.add(CASTDFRepository.sourceSignal(t))
        }
    }

    override val minX = axisX.minOrNull() ?: 0.0
    override val maxX = axisX.maxOrNull() ?: 0.0
    override val minY = axisY.minOrNull() ?: 0.0
    override val maxY = axisY.maxOrNull() ?: 0.0
}