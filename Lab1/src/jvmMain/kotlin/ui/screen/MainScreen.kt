package ui.screen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign


@Composable
@Preview
fun MainScreen() {
    Row {
        Text(
            textAlign = TextAlign.Center,
            text = "Спектральный анализ периодического сигнала в зависимости от относительной потери мощности"
        )
    }

    Row {

    }
//
//
//    a) рассчитать среднюю мощность для периодической последовательности импульсов Pc;
//
//    б) рассчитать значения амплитуд и фаз гармоник, определив их количество согласно относительной потери мощности  (1=0,1;2=0,05; 3=0,02; 4=0,01; 5=0,001);
//    в) построить амплитудный и фазовый спектры для максимального количества гармоник;
//    г) восстановить исходную периодическую последовательность S(t) с использованием полученных гармоник и построить 5 графиков
//    (согласно проведенным экспериментам) S(t) в зависимости от количества гармоник.
}


//@Composable
//@Preview
//fun App() {
//    var text by remember { mutableStateOf("Hello, World!") }
//
//    MaterialTheme {
//        Button(onClick = {
//            text = "Hello, Desktop!"
//        }) {
//            Text(text)
//        }
//    }
//}
