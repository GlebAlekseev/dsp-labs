package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.common.Repository
import com.glebalekseevjk.common.ui.widget.linechart.data.*
import com.glebalekseevjk.common.ui.widget.table.TableWidget
import ui.widget.MainWrapper
import ui.widget.linechart.amplitude.AmplitudeSpectrumPlot
import ui.widget.linechart.phase.PhaseSpectrumPlot
import ui.widget.linechart.sourcesignal.SourceSignalPlot

val deltaCases = mapOf(
    1 to Repository.VARIABLE_DELTA_CASE_1,
    2 to Repository.VARIABLE_DELTA_CASE_2,
    3 to Repository.VARIABLE_DELTA_CASE_3,
    4 to Repository.VARIABLE_DELTA_CASE_4,
    5 to Repository.VARIABLE_DELTA_CASE_5
)

val repository by lazy { Repository() }
val bList = mutableListOf<List<String>>()

@Composable
fun MainScreen() {
    deltaCases.forEach {
        val countHarmonics = repository.getMaxNumberOfHarmonics(it.value)
        val amplitude = repository.getAmplitude(countHarmonics)
        val phase = repository.getPhase(countHarmonics)
        bList.add(listOf("${it.value}", "$countHarmonics", "$amplitude", "$phase"))
    }

    Column {
        MainWrapper {
            Row {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = "Спектральный анализ периодического сигнала в зависимости от относительной потери мощности\nВариант 2"
                )
            }

            Row {
                TableWidget(
                    listOf(
                        listOf(
                            "${Repository.VARIABLE_EMax}",
                            "${Repository.VARIABLE_tu}",
                            "${Repository.VARIABLE_T}",
                            "${Repository.VARIABLE_W}"
                        ),
                    ),
                    listOf("Emax, В", "tи, с", "T, с", "w, рад/с"),
                    floatArrayOf(0.16f, 0.28f, 0.28f, 0.28f),
                    600.dp,
                    300.dp
                )
            }

            Row(modifier = Modifier.width(1000.dp)) {
                Text(
                    buildAnnotatedString {
                        append(
                            AnnotatedString(
                                "a) Рассчитать среднюю мощность для периодической последовательности импульсов Pc: ",
                                SpanStyle(Color.Black)
                            )
                        )
                        append(
                            AnnotatedString(
                                "${repository.getAveragePower()} Вт",
                                SpanStyle(color = Color.DarkGray, background = Color.Green)
                            )
                        )
                    }, modifier = Modifier
                        .padding(bottom = 24.dp)

                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text("б) Рассчитать значения амплитуд и фаз гармоник, определив их количество согласно относительной потери мощности:")
                TableWidget(
                    bList,
                    listOf("δ", "Количество гармоник", "Амплитуда n-ой гармоники, В", "Фаза n-ой гармоники, рад"),
                    floatArrayOf(0.16f, 0.28f, 0.28f, 0.28f),
                    600.dp,
                    500.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "в) Построить амплитудный и фазовый спектры для максимального количества гармоник;"
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum1,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_1}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum2,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_2}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum3,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_3}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum4,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_4}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum5,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_5}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum1,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_1}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum2,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_2}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum3,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_3}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum4,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_4}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum5,
                    "Фазовый спектр, δ = ${Repository.VARIABLE_DELTA_CASE_5}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "г) Восстановить исходную периодическую последовательность S(t) с использованием полученных гармоник и построить 5 графиков" +
                            " (согласно проведенным экспериментам) S(t) в зависимости от количества гармоник."
                )
                SourceSignalPlot(
                    modifier = Modifier.heightIn(0.dp, 800.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 200.dp)
                )
            }
        }
    }
}