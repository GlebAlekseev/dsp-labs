package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.glebalekseevjk.common.ui.widget.linechart.AnalogSignalPlot
import com.glebalekseevjk.common.ui.widget.linechart.DiscreteSignalPlot
import com.glebalekseevjk.common.ui.widget.linechart.ErrorQuantizedSignalPlot
import com.glebalekseevjk.common.ui.widget.linechart.QuantizedSignalPlot
import com.glebalekseevjk.common.ui.widget.linechart.data.AnalogSignalData
import com.glebalekseevjk.common.ui.widget.linechart.data.DiscreteSignalData
import com.glebalekseevjk.common.ui.widget.linechart.data.ErrorQuantizedSignalData
import com.glebalekseevjk.common.ui.widget.linechart.data.QuantizedSignalData
import com.glebalekseevjk.common.ui.widget.table.TableWidget
import compxclib.ComplexNumber
import ui.widget.MainWrapper
import ui.widget.linechart.sourcesignal.SourceSignalPlot

val castdfRepository = CASTDFRepository()

val boundaryFrequencyWk by lazy { castdfRepository.getSpectrumWidth() }
val boundaryFrequencyHz by lazy { castdfRepository.getSpectrumWidthHz() }

val samplingFrequency by lazy { castdfRepository.getSamplingFrequencyAnalogSignal() }
val rawSamplingStep by lazy { castdfRepository.getRawSamplingStep() }
val numberReadoutValues by lazy { castdfRepository.getNumberReadoutValues() }
val finalSamplingStep by lazy { castdfRepository.getFinalSamplingStep() }

val generatedDiscreteSignal by lazy { castdfRepository.getGeneratedDiscreteSignal() }
val quantizedSignal by lazy { castdfRepository.getQuantizedSignal(generatedDiscreteSignal) }

val discreteFourierTransformCoefficients by lazy {
    castdfRepository.getDiscreteFourierTransformCoefficients(
        quantizedSignal.first
    )
}

@Composable
fun MainScreen() {
    Column {
        MainWrapper {
            Row {
                Text(
                    modifier = Modifier
                        .padding(bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    text = "Преобразование аналогового сигнала в цифровой вид. Дискретное преобразование Фурье\nВариант 2"
                )
            }

            Row {
                TableWidget(
                    listOf(
                        listOf(
                            "${CASTDFRepository.VARIABLE_T}",
                            "${CASTDFRepository.VARIABLE_a}",
                            "${CASTDFRepository.VARIABLE_b}",
                        ),
                    ),
                    listOf("T, с", "a, 1/с", "b, В"),
                    floatArrayOf(0.34f, 0.33f, 0.33f),
                    width = 600.dp,
                    height = 500.dp,
                    headerCellHeight = 40.dp,
                    contentCellHeight = 40.dp,
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 10.dp, top = 20.dp),
                    textAlign = TextAlign.Left,
                    text = "1. Построение графика аналогового сигнала."
                )
                AnalogSignalPlot(
                    AnalogSignalData,
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 20.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    "2. Определение граничной частоты (верхней частоты) аналогового сигнала."
                )
                TableWidget(
                    listOf(
                        listOf(
                            "${CASTDFRepository.VARIABLE_DELTA}",
                            "${boundaryFrequencyWk.toLong()}",
                            "${boundaryFrequencyHz.toLong()}"
                        )
                    ),
                    listOf("δ", "Верхняя частота, рад/с", "Верхняя частота, Гц"),
                    floatArrayOf(0.33f, 0.33f, 0.34f),
                    width = 600.dp,
                    height = 500.dp,
                    headerCellHeight = 60.dp,
                    contentCellHeight = 40.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    "3. Определение частоты дискретизации аналогового сигнала."
                )
                TableWidget(
                    listOf(
                        listOf(
                            "${samplingFrequency.toLong()}",
                            "$rawSamplingStep",
                            "$numberReadoutValues",
                            "$finalSamplingStep",
                        )
                    ),
                    listOf(
                        "Частота дискретизации аналогового сигнала, Гц",
                        "Шаг дискретизации, с",
                        "Количество отсчетных значений",
                        "Шаг дискретизации (по выбранному значению отсчетов), с"
                    ),
                    floatArrayOf(.25f, .25f, .25f, .25f),
                    width = 600.dp,
                    height = 500.dp,
                    headerCellHeight = 110.dp,
                    contentCellHeight = 60.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "4. Генерирование дискретного сигнала в виде массива чисел и построение его графика."
                )

                TableWidget(
                    generatedDiscreteSignal.map { listOf(it.first.toString(), it.second.toString()) }.reversed(),
                    listOf(
                        "Время, с",
                        "S(t), В",
                    ),
                    floatArrayOf(.5f, .5f),
                    width = 600.dp,
                    height = 600.dp,
                    headerCellHeight = 40.dp,
                    contentCellHeight = 40.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                DiscreteSignalPlot(
                    DiscreteSignalData,
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 20.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "5. Выполнение квантования дискретного сигнала с шагом, соответствующим представлению отсчетов " +
                            "4-битным числом."
                )

                TableWidget(
                    quantizedSignal.first.map { listOf(it.first.toString(), it.second.toString()) }.reversed(),
                    listOf(
                        "Время, с",
                        "S(t), В",
                    ),
                    floatArrayOf(.5f, .5f),
                    width = 600.dp,
                    height = 600.dp,
                    headerCellHeight = 40.dp,
                    contentCellHeight = 40.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "6. Построение графика квантованного сигнала с изображением уровней квантования в виде шестнадцатиричного кода."
                )

                QuantizedSignalPlot(
                    QuantizedSignalData,
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 20.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "7. Вычисление погрешности квантования. Построение графика погрешности."
                )

                ErrorQuantizedSignalPlot(
                    ErrorQuantizedSignalData,
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 20.dp)
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "8. Определение коэффициентов ДПФ."
                )

                TableWidget(
                    discreteFourierTransformCoefficients.mapIndexed { index: Int, complexNumber: ComplexNumber ->
                        listOf(index.toString(), complexNumber.toString())
                    },
                    listOf(
                        "№",
                        "Коэффициент",
                    ),
                    floatArrayOf(.1f, .9f),
                    width = 600.dp,
                    height = 600.dp,
                    headerCellHeight = 40.dp,
                    contentCellHeight = 40.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }

            Column(modifier = Modifier.width(1000.dp)) {
                Text(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 24.dp),
                    textAlign = TextAlign.Left,
                    text = "9. Построение и сравнение графиков исходного аналогового сигнала с восстановленным."
                )

                SourceSignalPlot(
                    modifier = Modifier.heightIn(0.dp, 600.dp).widthIn(0.dp, 1000.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp, bottom = 60.dp)
                )
            }
        }
    }
}