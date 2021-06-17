package com.example.compose.rally.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Utility class to convert HEX string to Color and vice versa.
 */
class ColorConverter {
    companion object {
        fun getColor(colorString: String): Color {
            return Color(android.graphics.Color.parseColor("#" + colorString))
        }
        fun getHex(color: Color): String {
            return java.lang.String.format("%06X", color.toArgb() and 0xFFFFFF)
        }
    }
}