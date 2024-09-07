package com.tunahankaryagdi.firstproject.utils.ext

import java.util.Locale


fun Double.toFormattedString(): String {
    return String.format(Locale.getDefault(), "%.1f", this)
}