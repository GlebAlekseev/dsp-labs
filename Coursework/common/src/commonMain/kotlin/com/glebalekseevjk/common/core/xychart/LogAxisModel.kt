package io.github.koalaplot.core.xychart

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

private const val Base = 10.0
private val MinorTickScale = 2..9

/**
 * A logarithmic axis.
 *
 * @param range  The minimum to maximum values allowed to be represented on this Axis expressed as
 * exponents with a base of 10. For example a range of -1..3 represents an axis range of 0.1 to 1000
 * (10^-1..10^3).
 */
public class LogAxisModel constructor(
    private val range: ClosedRange<Double>,
    override val minimumMajorTickSpacing: Dp = 50.dp,
) : AxisModel<Double> {
    init {
        require(range.endInclusive > range.start) { "Axis end must be greater than start" }
    }

    override fun computeOffset(point: Double): Float {
        return (
                (log(point.toDouble(), Base) - range.start) /
                        (range.endInclusive - range.start)
                ).toFloat()
    }

    private fun computeMinorTickValues(majorTickValues: List<Float>): List<Float> = buildList {
        for (tick in 0 until majorTickValues.lastIndex) {
            val init = majorTickValues[tick]
            for (i in MinorTickScale) {
                add(init * i)
            }
        }
    }

    override fun computeTickValues(axisLength: Dp): TickValues<Double> {

        val majorTickValues = buildList {
            for (i in range.start.toInt()..range.endInclusive.toInt()) {
                for (j in 0..4){
                    add(Base.pow(i + j*0.2))
                }
            }
        }

        return object : TickValues<Double> {
            override val majorTickValues = majorTickValues
            override val minorTickValues = computeMinorTickValues(majorTickValues.map { it.toFloat() }).map { it.toDouble() }
        }
    }
}
