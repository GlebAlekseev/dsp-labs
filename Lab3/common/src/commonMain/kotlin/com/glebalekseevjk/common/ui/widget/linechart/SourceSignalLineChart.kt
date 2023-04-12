package ui.widget.linechart.sourcesignal

import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.legend.FlowLegend
import io.github.koalaplot.core.legend.LegendLocation
import io.github.koalaplot.core.line.DefaultPoint
import io.github.koalaplot.core.line.LineChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.VerticalRotation
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.util.rotateVertically
import io.github.koalaplot.core.xychart.*
import io.github.koalaplot.sample.*
import ui.screen.repository


internal object SourceSignal {
    var timeList = mutableListOf(0.0)
    val amplitudesList = listOf(
        "Исходный" to mutableListOf<Double>(),
        "Восстановленный" to mutableListOf<Double>(),
    )

    init {
        val interval = CASTDFRepository.VARIABLE_T / 100

        val maxTime = CASTDFRepository.VARIABLE_T
        val countRepeat: Int = (maxTime / interval).toInt() + 1
        var timer = 0.0
        repeat(countRepeat) {
            timer += interval
            timeList.add(timer)
        }
        val discreteSignal = repository.getGeneratedDiscreteSignal()
        val quantizedSignal = repository.getQuantizedSignal(discreteSignal)
        val koefs = repository.getDiscreteFourierTransformCoefficients(quantizedSignal.first)

        timeList.forEach { time ->
            amplitudesList[0].second.add(CASTDFRepository.sourceSignal(time))
        }
        timeList.forEach { time ->
            amplitudesList[1].second.add(repository.getRestoredDpfSignal(koefs, time))
        }
    }

    val minY = SourceSignal.amplitudesList.map { it.second.minOrNull() ?: Double.MAX_VALUE }.minOrNull() ?: 0.0
    val maxY = SourceSignal.amplitudesList.map { it.second.maxOrNull() ?: Double.MIN_VALUE }.maxOrNull() ?: 0.0

    val minX = timeList.minOrNull() ?: 0.0
    val maxX = timeList.maxOrNull() ?: 0.0

    val colorMap = buildMap {
        val colors = generateHueColorPalette(amplitudesList.size)
        var i = 0
        amplitudesList.forEach {
            put(it.first, colors[i++])
        }
    }
}


@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
@Suppress("MagicNumber")
fun SourceSignalPlot(modifier: Modifier = Modifier) {
    val title = "Восстановленный аналоговый сигнал"
    ChartLayout(
        modifier = modifier,
        title = { ChartTitle(title) },
        legend = { Legend() },
        legendLocation = LegendLocation.BOTTOM
    ) {
        XYChart(
            xAxisModel = LinearAxisModel(
                ((SourceSignal.minX..SourceSignal.maxX)),
            ),
            yAxisModel = LinearAxisModel(
                ((SourceSignal.minY)..(SourceSignal.maxY)),
                minimumMajorTickSpacing = 5.dp,
            ),
            xAxisLabels = {
                AxisLabel(it.toString(), Modifier.padding(top = 2.dp))
            },
            xAxisTitle = { AxisTitle("Время, с") },
            yAxisLabels = {
                AxisLabel(it.toString(), Modifier.absolutePadding(right = 2.dp))
            },
            yAxisTitle = {
                AxisTitle(
                    "S(t)",
                    modifier = Modifier.rotateVertically(VerticalRotation.COUNTER_CLOCKWISE)
                        .padding(bottom = padding)
                )
            }
        ) {
            SourceSignal.amplitudesList.forEach {
                chart(
                    it.first,
                    it.second.mapIndexed { index: Int, d: Double ->
                        DefaultPoint(SourceSignal.timeList[index], d)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun XYChartScope<Double, Double>.chart(
    caseName: String,
    data: List<DefaultPoint<Double, Double>>,
) {
    LineChart(
        data = data,
        splineMinWeight = 100.0,
        lineStyle = LineStyle(
            brush = SolidColor(SourceSignal.colorMap[caseName] ?: Color.Black),
        ),
        symbol = { point ->
            Symbol(
                size = 5.dp,
                shape = RoundedCornerShape(50),
                fillBrush = SolidColor(SourceSignal.colorMap[caseName] ?: Color.Black),
                modifier = Modifier.then(
                    Modifier.hoverableElement {
                        HoverSurface { Text(point.y.toString()) }
                    }
                )
            )
        }
    )
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Legend() {
    val cases = SourceSignal.amplitudesList.map { it.first }.sorted()

    Surface(elevation = 2.dp) {
        FlowLegend(
            itemCount = cases.size,
            symbol = { i ->
                Symbol(
                    modifier = Modifier.size(padding),
                    fillBrush = SolidColor(SourceSignal.colorMap[cases[i]] ?: Color.Black)
                )
            },
            label = { i ->
                Text(cases[i])
            },
            modifier = paddingMod
        )
    }
}
