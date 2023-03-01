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
import com.glebalekseevjk.common.SANPSRepository
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
import ui.screen.sanpsRepository

val deltaCases = mapOf(
    1 to SANPSRepository.VARIABLE_DELTA_CASE_1,
    2 to SANPSRepository.VARIABLE_DELTA_CASE_2,
    3 to SANPSRepository.VARIABLE_DELTA_CASE_3,
    4 to SANPSRepository.VARIABLE_DELTA_CASE_4,
    5 to SANPSRepository.VARIABLE_DELTA_CASE_5
)

internal object SourceSignal {
    var timeList = mutableListOf(0.0)
    val amplitudesList = listOf(
        "Эксперимент 1: δ = ${SANPSRepository.VARIABLE_DELTA_CASE_1}" to mutableListOf<Double>(),
        "Эксперимент 2: δ = ${SANPSRepository.VARIABLE_DELTA_CASE_2}" to mutableListOf<Double>(),
        "Эксперимент 3: δ = ${SANPSRepository.VARIABLE_DELTA_CASE_3}" to mutableListOf<Double>(),
        "Эксперимент 4: δ = ${SANPSRepository.VARIABLE_DELTA_CASE_4}" to mutableListOf<Double>(),
        "Эксперимент 5: δ = ${SANPSRepository.VARIABLE_DELTA_CASE_5}" to mutableListOf<Double>(),
    )

    init {
        val interval = 0.000001

        val maxTime = SANPSRepository.VARIABLE_tu
        val countRepeat: Int = (maxTime / interval).toInt() + 1
        println(":>>>>>>>>>>>countRepeat=${countRepeat}")
        var timer = 0.0
        repeat(countRepeat) {
            timer += interval
            timeList.add(timer)
        }
        deltaCases.forEach {
            val n = sanpsRepository.getSpectrumWidth(it.value)
//            val n = 340448.0
//            val n = 190448.0
            timeList.forEach { time ->
                amplitudesList[it.key - 1].second.add(sanpsRepository.getSignalSpectralDensitySt(n, time))
            }
//            return@forEach
        }
        println(">>>>>>>>>>>>>>>> ")
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
    val title = "Исходный одиночный импульс S(t)"
    ChartLayout(
        modifier = modifier,
        title = { ChartTitle(title) },
        legend = { Legend() },
        legendLocation = LegendLocation.BOTTOM
    ) {
        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> (SourceSignal.minX.toFloat()..SourceSignal.maxX.toFloat())=${(SourceSignal.minX.toFloat()..SourceSignal.maxX.toFloat())}")
        XYChart(
//            xAxisModel = CategoryAxisModel(AmplitudeSpectrum.harmonicList),
            xAxisModel = LinearAxisModel(
                ((SourceSignal.minX.toFloat()..SourceSignal.maxX.toFloat())
                    .also { println(">>>>>>>>>>>>>>>>>>>>> range =${it.toString()}") }),
            ),
            yAxisModel = LinearAxisModel(
                ((SourceSignal.minY * 1.1).toFloat()..(SourceSignal.maxY * 1.1).toFloat()).also { println(">>>>>>>>>>>>>>> rangeY=${it}") },
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
                        DefaultPoint(SourceSignal.timeList[index].toFloat(), d.toFloat())
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun XYChartScope<Float, Float>.chart(
    caseName: String,
    data: List<DefaultPoint<Float, Float>>,
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
