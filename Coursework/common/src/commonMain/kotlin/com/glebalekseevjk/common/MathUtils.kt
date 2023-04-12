package com.glebalekseevjk.common

fun Double.roundToString(n: Int) = "%.${n}f".format(this).replace(Regex("\\,*\\.*0+$"), "")

fun Double.roundUp(): Double = if (this - this.toInt() == 0.0) this else this.toInt() + 1.0

fun Double.roundUpToEven(): Int {
    val integer = this.toInt()
    val decimal = this - integer
    when (integer % 2) {
        0 -> {
            if (decimal == 0.0) return integer
            return integer + 2
        }
        1 -> return integer + 1
        else -> return integer
    }
}