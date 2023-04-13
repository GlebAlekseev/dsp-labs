package com.glebalekseevjk.common.ui.widget.linechart

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.common.roundToString
import com.glebalekseevjk.common.ui.widget.data.LineChartData
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.line.DefaultPoint
import io.github.koalaplot.core.line.LineChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xychart.*
import io.github.koalaplot.sample.*
import kotlin.math.absoluteValue

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
@Suppress("MagicNumber")
fun DiscreteSignalPlot(
    data: LineChartData,
    title: String = "Дискретный сигнал",
    modifier: Modifier = Modifier
) {
    ChartLayout(
        modifier = modifier,
        title = { ChartTitle(title) },
    ) {
        val deltaY = (data.maxY!! - data.minY!!).absoluteValue
        XYChart<Double, Double>(
            xAxisModel = LinearAxisModel(
                (data.minX!!..data.maxX!!),
                minimumMajorTickSpacing = 25.dp,
                minimumMajorTickIncrement = 0.01,
                minorTickCount = 1
            ),
            yAxisModel = LinearAxisModel(
                ((if (data.minY!! <= 0) data.minY!! - deltaY * 0.1 else data.minY!! + deltaY * 0.1)..
                        (if (data.maxY!! <= 0) data.maxY!! - deltaY * 0.1 else data.maxY!! + deltaY * 0.1)),
                minimumMajorTickSpacing = 25.dp,
                minimumMajorTickIncrement = 0.001,
                minorTickCount = 1
            ),
            xAxisLabels = {
                AxisLabel(
                    it.roundToString(2),
                )
            },
            xAxisTitle = { AxisTitle("Время, с") },
            yAxisLabels = {
                AxisLabel(it.roundToString(2))
            },
            yAxisTitle = {
                AxisTitle(
                    "Амплитуда, В",
                    modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        .padding(bottom = padding)
                )
            }
        ) {
            chart(
                data.axisY.mapIndexed { index: Int, d: Double ->
                    DefaultPoint(data.axisX[index], d)
                }
            )
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun XYChartScope<Double, Double>.chart(
    data: List<DefaultPoint<Double, Double>>
) {
    LineChart(
        isSpline = false,
        data = data,
        lineStyle = LineStyle(
            brush = SolidColor(null ?: Color.Black),
            strokeWidth = 2.dp
        )
    )
}
