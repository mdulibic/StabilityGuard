package com.example.stabilityGuard.utils

import android.content.res.Resources
import java.text.DecimalFormat

/**
 *
 */

fun Int.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

private const val TWO_DECIMALS_FORMAT = "#.##"

fun Double.formatToTwoDecimals(): String {
    val decimalFormat = DecimalFormat(TWO_DECIMALS_FORMAT)
    decimalFormat.minimumFractionDigits = 2
    decimalFormat.maximumFractionDigits = 2
    return decimalFormat.format(this)
}