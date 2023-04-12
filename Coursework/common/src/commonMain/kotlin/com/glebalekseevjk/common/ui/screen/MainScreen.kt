package com.glebalekseevjk.common.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.glebalekseevjk.common.ResultData
import com.glebalekseevjk.common.roundToString
import com.glebalekseevjk.common.ui.widget.data.*
import com.glebalekseevjk.common.ui.widget.linechart.QuantizedSignalPlot
import com.glebalekseevjk.common.ui.widget.table.TableWidget
import ui.widget.MainWrapper
import ui.widget.linechart.sourcesignal.CombinedLineChartPlot

val repository = Repository()

@Composable
fun MainScreen() {
    val resultDataState by repository.resultDataState.collectAsState()
    Column {
        MainWrapper {
            Row {
                Text(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = "Обработка сигналов цифровым фильтром\nВариант 2"
                )
            }

            // Таблица fс fз Aп Aз fд f1 f2 f3 f4 f5
            Column(modifier = Modifier.width(1000.dp)) {
                Text("Дано по условию", modifier = Modifier.align(Alignment.CenterHorizontally))
                TableWidget(
                    listOf(
                        listOf(
                            ResultData.F_C.roundToString(2),
                            ResultData.F_Z.roundToString(2),
                            ResultData.A_P.roundToString(2),
                            ResultData.A_Z.roundToString(2),
                            ResultData.F_D.roundToString(2),
                            ResultData.F_1.roundToString(2),
                            ResultData.F_2.roundToString(2),
                            ResultData.F_3.roundToString(2),
                            ResultData.F_4.roundToString(2),
                            ResultData.F_5.roundToString(2),
                        )
                    ),
                    listOf(
                        "fс, Гц",
                        "fз, Гц",
                        "Aп, дБ",
                        "Aз, дБ",
                        "fд, Гц",
                        "f1, Гц",
                        "f2, Гц",
                        "f3, Гц",
                        "f4, Гц",
                        "f5, Гц"
                    ),
                    floatArrayOf(0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f),
                    width = 600.dp,
                    height = 500.dp,
                    headerCellHeight = 60.dp,
                    contentCellHeight = 40.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    "1) Рассчитать вспомогательные параметры АЧХ:\n" +
                            "δ1 = ${resultDataState.delta1.roundToString(3)}\n" +
                            "δ2 = ${resultDataState.delta2.roundToString(3)}\n" +
                            "min(δ1, δ2) = ${resultDataState.delta.roundToString(3)}", modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                Text(
                    "2) Рассчитать ширину переходной полосы и середину переходной полосы для АЧХ (ФНЧ):\n" +
                            "Δfпер = ${resultDataState.fTransition.roundToString(3)} Гц\n" +
                            "fc1р = ${resultDataState.fMiddleTransition.roundToString(3)} Гц", modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                Text(
                    "3) Рассчитать порядок фильтра M, используя значение D-фактора по весовым функциям Кайзера:\n" +
                            "A = ${resultDataState.a.roundToString(3)}\n" +
                            "D = ${resultDataState.d.roundToString(3)}\n" +
                            "M = ${resultDataState.m}\n" +
                            "N = ${resultDataState.n}", modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                Text(
                    "4) Рассчитать параметр весовой функции Кайзера α:\n" +
                            "α = ${resultDataState.alpha.roundToString(2)}", modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                val annotatedStringAlphaI = buildAnnotatedString {
                    append(
                        AnnotatedString(
                            "5) Рассчитать коэффициенты Фурье αi:\n",
                            SpanStyle(Color.Black)
                        )
                    )
                    var i = 0
                    while (i < resultDataState.m / 2 + 1) {
                        append(
                            AnnotatedString(
                                "α${i}".padEnd(4, '⠀') + "= ${
                                    resultDataState.alphaI(i).roundToString(2).replace(Regex("-0$"), "0")
                                }\n",
                                SpanStyle(Color.Black)
                            )
                        )
                        i++
                    }
                }

                Text(
                    annotatedStringAlphaI, modifier = Modifier
                        .padding(bottom = 24.dp)
                )

                val annotatedStringHI = buildAnnotatedString {
                    append(
                        AnnotatedString(
                            "6) Вычислить коэффициенты фильтра bi:\n",
                            SpanStyle(Color.Black)
                        )
                    )
                    var i = 0
                    while (i <= resultDataState.m) {
                        append(
                            AnnotatedString(
                                "b${i}".padEnd(4, '⠀') + "= ${
                                    resultDataState.hi(i).roundToString(2).replace(Regex("-0$"), "0")
                                }\n",
                                SpanStyle(Color.Black)
                            )
                        )
                        i++
                    }

                }

                Text(
                    annotatedStringHI, modifier = Modifier
                        .padding(bottom = 24.dp)
                )
            }

            // 7) Рассчитать АЧХ фильтра и проверить ее соответствие заданным требованиям.
            // Нормировать и представить в логарифмическом масштабе
            // 10) Импульсная характеристика фильтра строится по полученным отсчетным
            //значениям (11).

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "7,10) Импульсная характеристика фильтра строится по полученным отсчетным значениям."
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        ImpulseData(
                            with(resultDataState) {
                                listOf(
                                    Pair(impulseResponse.axisX, impulseResponse.axisY),
                                )
                            }

                        ),
                        title = "Импульсная характеристика фильтра",
                        axisXLabel = "№ отсчетного значения",
                        axisYLabel = "Импульс"
                    )
                }
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "11) Рассчитать АЧХ фильтра и проверить ее соответствие заданным требованиям."
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        LPFAmplitudeData(
                            with(resultDataState) {
                                listOf(
                                    Pair(amplitudeFrequencyResponse.axisX, amplitudeFrequencyResponse.axisY),
                                    Pair(requiredCharacteristicsOfLPF.axisX, requiredCharacteristicsOfLPF.axisY),
                                    Pair(lowerBandwidthLimit.axisX, lowerBandwidthLimit.axisY),
                                    Pair(upperBandwidthLimit.axisX, upperBandwidthLimit.axisY),
                                    Pair(upperLimitOfDelayBand.axisX, upperLimitOfDelayBand.axisY),
                                )
                            }

                        ),
                        title = "АЧХ НРЦФ",
                        axisXLabel = "w, рад/с",
                        axisYLabel = "A, дБ"
                    )
                }
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        PhaseData(
                            with(resultDataState) {
                                listOf(
                                    Pair(phaseFrequencyResponse.axisX, phaseFrequencyResponse.axisY),
                                )
                            }

                        ),
                        title = "ФЧХ НРЦФ",
                        axisXLabel = "Частота, рад/с",
                        axisYLabel = "Фаза, рад"
                    )
                }
            }

            // на листке записать схему + программно выводить кол-во ПЗУ/ОЗУ ...
            // ИХ АЧХ ФЧХ


            // показать исходный сигнал

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "12) Исходный тестовый сигнал"
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        SourceData(
                            with(resultDataState) {
                                listOf(
                                    Pair(sourceSignal.axisX, sourceSignal.axisY),
                                )
                            }

                        ),
                        title = "Исходный тестовый сигнал",
                        axisXLabel = "Время, с",
                        axisYLabel = "Амплитуда, дБ",
                        isSpline = true
                    )
                }
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "12) Дискретизация тестового сигнала fд=128 Гц\n" +
                            "Tд = ${resultDataState.rawSamplingStep.roundToString(3)}\n" +
                            "N = ${resultDataState.numberReadoutValues}\n" +
                            "fin Tд = ${resultDataState.finalSamplingStep.roundToString(3)}"
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        DiscreteData(
                            with(resultDataState) {
                                listOf(
                                    Pair(discreteSignal.axisX, discreteSignal.axisY),
                                )
                            }

                        ),
                        title = "Дискретный сигнал",
                        axisXLabel = "Время, с",
                        axisYLabel = "Амплитуда, дБ",
                        isSpline = false
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "Шаг квантования Δ: ${resultDataState.quantizationStep.roundToString(3)}"
                )
                Column(modifier = Modifier.heightIn(0.dp, 900.dp).width(800.dp).padding(15.dp)) {
                    QuantizedSignalPlot(
                        QuantizedSignalData
                    )
                }
            }

            // дискретный и квантованный сигнал
            // 15) Аналитически сигнал на выходе фильтра имеет следующий вид
            // Аналитический сигнал на выходе фильтра
            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "14-15) Аналитически сигнал на выходе фильтра имеет следующий вид:"
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        AnalyticalOutputData(
                            with(resultDataState) {
                                listOf(
                                    Pair(outputAnalyticalSignal.axisX, outputAnalyticalSignal.axisY),
                                )
                            }

                        ),
                        title = "Аналитический сигнал на выходе фильтра",
                        axisXLabel = "Время, с",
                        axisYLabel = "Амплитуда, дБ",
                        isSpline = true
                    )
                }
            }

            // 16) Отсчетные значения цифрового сигнала на выходе фильтра
            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "16) Отсчетные значения цифрового сигнала на выходе фильтра:"
                )
                Column(modifier = Modifier.heightIn(0.dp, 600.dp).width(800.dp).padding(15.dp)) {
                    CombinedLineChartPlot(
                        AnalyticalOutputData(
                            with(resultDataState) {
                                listOf(
                                    Pair(outputReferencesValuesSignal.axisX, outputReferencesValuesSignal.axisY),
                                )
                            }

                        ),
                        title = "Отсчетные значения цифрового сигнала на выходе фильтра",
                        axisXLabel = "№ отсчетного значения",
                        axisYLabel = "Амплитуда, дБ",
                        isSpline = false
                    )
                }
            }
        }
    }
}