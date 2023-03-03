package ui.widget.linechart.amplitude

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
import kotlin.math.ceil

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
@Suppress("MagicNumber")
fun AmplitudeSpectrumPlot(data: LineChartData, title: String = "Амплитудный спектр", modifier: Modifier = Modifier) {
    ChartLayout(
        modifier = modifier,
        title = { ChartTitle(title) },
    ) {
        XYChart(
            xAxisModel = LinearAxisModel(
                (data.minX.toFloat()..data.maxX.toFloat()),
                minimumMajorTickSpacing = 50.dp,
                minimumMajorTickIncrement = 1.0f
            ),
            yAxisModel = LinearAxisModel(
                0f..(ceil(data.maxY / 50.0) * 10.0).toFloat(),
            ),
            xAxisLabels = {
                AxisLabel(it.toInt().toString(), Modifier.padding(top = 2.dp))
            },
            xAxisTitle = { AxisTitle("Гармоника") },
            yAxisLabels = {
                AxisLabel(it.toString(), Modifier.absolutePadding(right = 2.dp))
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
                    DefaultPoint(data.axisX[index].toFloat(), d.toFloat())
                }
            )
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun XYChartScope<Float, Float>.chart(
    data: List<DefaultPoint<Float, Float>>
) {
    LineChart(
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