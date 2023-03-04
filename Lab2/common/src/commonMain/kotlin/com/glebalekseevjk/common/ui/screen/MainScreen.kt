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
import com.glebalekseevjk.common.SANPSRepository
import com.glebalekseevjk.common.ui.widget.linechart.AmplitudeSpectrumPlot
import com.glebalekseevjk.common.ui.widget.linechart.PhaseSpectrumPlot
import com.glebalekseevjk.common.ui.widget.linechart.data.*
import com.glebalekseevjk.common.ui.widget.table.TableWidget
import ui.widget.MainWrapper
import ui.widget.linechart.sourcesignal.SourceSignalPlot

val deltaCases = mapOf(
    1 to SANPSRepository.VARIABLE_DELTA_CASE_1,
    2 to SANPSRepository.VARIABLE_DELTA_CASE_2,
    3 to SANPSRepository.VARIABLE_DELTA_CASE_3,
    4 to SANPSRepository.VARIABLE_DELTA_CASE_4,
    5 to SANPSRepository.VARIABLE_DELTA_CASE_5
)

val sanpsRepository by lazy { SANPSRepository() }
val bList = mutableListOf<List<String>>()

@Composable
fun MainScreen() {
    deltaCases.forEach {
        val spectrumWidth = sanpsRepository.getSpectrumWidth(it.value)
        bList.add(listOf("${it.value}", "$spectrumWidth"))
    }

    Column {
        MainWrapper {
            Row {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = "Спектральный анализ непериодического сигнала в зависимости от относительной потери энергии\nВариант 2"
                )
            }

            Row {
                TableWidget(
                    listOf(
                        listOf(
                            "${SANPSRepository.VARIABLE_EMax}",
                            "${SANPSRepository.VARIABLE_tu}",
                        ),
                    ),
                    listOf("Emax, В", "tи, с"),
                    floatArrayOf(0.5f, 0.5f),
                    600.dp,
                    300.dp
                )
            }

            Row(modifier = Modifier.width(1000.dp)) {
                Text(
                    buildAnnotatedString {
                        append(
                            AnnotatedString(
                                "a) Рассчитать энергию одиночного импульса Ec: ",
                                SpanStyle(Color.Black)
                            )
                        )
                        append(
                            AnnotatedString(
                                "${sanpsRepository.getTotalEnergy()} Дж",
                                SpanStyle(color = Color.DarkGray, background = Color.Green)
                            )
                        )
                    }, modifier = Modifier
                        .padding(bottom = 24.dp)
                )
            }

            Row(modifier = Modifier.width(1000.dp)) {
                Text(
                    buildAnnotatedString {
                        append(
                            AnnotatedString(
                                "б) Получить аналитическое выражение для спектральной плотности сигнала S(jw): ",
                                SpanStyle(Color.Black)
                            )
                        )
                        append(
                            AnnotatedString(
                                "Emax/w * sin(w*tu) + j(Emax/w * (cos(w * tu) - 1))",
                                SpanStyle(color = Color.DarkGray, background = Color.Green)
                            )
                        )
                    }, modifier = Modifier
                        .padding(bottom = 24.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    "в) Определить верхние частоты одиночного импульса, используя энергетический критерий, " +
                            "для различных значений погрешности δ"
                )
                TableWidget(
                    bList,
                    listOf("δ", "Верхняя частота, рад/с"),
                    floatArrayOf(0.5f, 0.5f),
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
                    text = "г) Построить АЧХ и ФЧХ одиночного импульса в зависимости от " +
                            "полученного значения верхней частоты (5 графиков АЧХ и 5 графиков ФЧХ)"
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum1,
                    "АЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_1}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum2,
                    "АЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_2}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum3,
                    "АЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_3}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum4,
                    "АЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_4}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )
                AmplitudeSpectrumPlot(
                    AmplitudeSpectrum5,
                    "АЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_5}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                )

                PhaseSpectrumPlot(
                    PhaseSpectrum1,
                    "ФЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_1}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum2,
                    "ФЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_2}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum3,
                    "ФЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_3}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum4,
                    "ФЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_4}",
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 40.dp, bottom = 40.dp)
                )
                PhaseSpectrumPlot(
                    PhaseSpectrum5,
                    "ФЧХ одиночного импульса, δ = ${SANPSRepository.VARIABLE_DELTA_CASE_5}",
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
                    text = "д) Восстановить исходной одиночный импульс S(t) по виду " +
                            "спектральной плотности S(jw) и построить 5 графиков (согласно " +
                            "проведенным экспериментам) S(t) с учетом разных значений верхних частот."
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