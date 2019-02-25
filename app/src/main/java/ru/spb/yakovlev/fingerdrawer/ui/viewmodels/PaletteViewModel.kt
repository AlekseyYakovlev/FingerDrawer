package ru.spb.yakovlev.fingerdrawer.ui.viewmodels

import android.graphics.Color.*
import androidx.lifecycle.ViewModel

class PaletteViewModel : ViewModel() {
    val colors = listOf(BLACK, WHITE, RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA)
    var currentColor = BLACK
    val frameColor = DKGRAY
    val selectionColor = YELLOW

    var radius: Int = 0
    val palette = mutableListOf<PaletteColor>()

    fun setupRadius(measuredWidth: Int) {
        radius = measuredWidth / (2 * colors.size)
    }

    fun setupPalette() {
        if (palette.isEmpty()) {
            addColorsToPalette(colors)
            palette.first { it.color == currentColor }.selected = true
//            colorChangeListener?.colorChanged(defaultColor)
        }
    }

    fun clearPalette() {
        palette.clear()
    }


    private fun addColorsToPalette(colors: List<Int>) {
        val radius = radius.toFloat()
        val x = radius
        val y = radius

        colors.forEachIndexed { index, color ->
            palette.add(
                PaletteColor(
                    color,
                    x + x * 2 * index,
                    y
                )
            )
        }
    }


    class PaletteColor(
        val color: Int,
        val x: Float,
        val y: Float,
        var selected: Boolean = false
    )

}