package com.glebalekseevjk.common.ui.widget.linechart.data

interface LineChartData {
    val minX: Double
    val minY: Double
    val maxX: Double
    val maxY: Double
    var axisX: MutableList<Double>
    var axisY: MutableList<Double>
}