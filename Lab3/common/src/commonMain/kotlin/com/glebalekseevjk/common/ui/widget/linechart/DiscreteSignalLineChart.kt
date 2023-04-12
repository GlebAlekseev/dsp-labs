package com.glebalekseevjk.common.ui.widget.linechart

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.common.ui.widget.linechart.data.LineChartData
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.line.DefaultPoint
import io.github.koalaplot.core.line.LineChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xychart.*
import io.github.koalaplot.sample.*


@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
@Suppress("MagicNumber")
fun DiscreteSignalPlot(data: LineChartData, title: String = "Дискретный сигнал", modifier: Modifier = Modifier) {
    ChartLayout(
        modifier = modifier,
        title = { ChartTitle(title) },
    ) {
        XYChart(
            xAxisModel = LinearAxisModel(
                (data.minX..data.maxX),
                minimumMajorTickSpacing = 50.dp,
                minimumMajorTickIncrement = 0.0000001
            ),
            yAxisModel = LinearAxisModel(
                data.minY..data.maxY,
                minimumMajorTickSpacing = 5.dp,
            ),
            xAxisLabels = {
                AxisLabel(it.toString(), Modifier.padding(top = 2.dp))
            },
            xAxisTitle = { AxisTitle("Время, с") },
            yAxisLabels = {
                AxisLabel(String.format("%.7f", it), Modifier.absolutePadding(right = 2.dp))
            },
            yAxisTitle = {
                AxisTitle(
                    "S(t), В",
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
        ),
        symbol = { point ->
            Symbol(
                size = 5.dp,
                shape = RoundedCornerShape(50),
                fillBrush = SolidColor(null ?: Color.Black),
                modifier = Modifier.then(
                    Modifier.hoverableElement {
                        HoverSurface { Text(point.y.toString()) }
                    }
                )
            )
        }
    )
}