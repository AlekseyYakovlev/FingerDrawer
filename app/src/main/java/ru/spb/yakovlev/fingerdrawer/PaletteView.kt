package ru.spb.yakovlev.fingerdrawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color.*
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow

class PaletteView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val colors = listOf(BLACK, WHITE, RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA)
    val defaultColor = BLACK
    val frameColor = DKGRAY
    val selectionColor = YELLOW

    private var colorChangeListener: OnColorChangeListener? = null

    private var radius: Int = 0
    private val palette = mutableListOf<PaletteColor>()
    private val tempPaint = Paint()


    fun setOnColorChangeListener(changeListener: OnColorChangeListener) {
        colorChangeListener = changeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        radius = measuredWidth / (2 * colors.size)

        if (palette.isEmpty()) {
            addColorsToPalette(colors)
            palette.first { it.color == defaultColor }.selected = true
            colorChangeListener?.colorChanged(defaultColor)
        }

        setMeasuredDimension(radius * palette.size * 2, radius * 2)
    }


    private fun addColorsToPalette(colors: List<Int>) {
        val radius = radius.toFloat()
        val x = radius
        val y = radius

        colors.forEachIndexed { index, color ->
            palette.add(PaletteColor(color, x + x * 2 * index, y))
        }

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        palette.forEach {
            tempPaint.color = frameColor
            canvas?.drawCircle(it.x, it.y, radius.toFloat() - 1f, tempPaint)
            if (it.selected) {
                tempPaint.color = selectionColor
                canvas?.drawCircle(it.x, it.y, radius.toFloat() - 6f, tempPaint)
                tempPaint.color = frameColor
                canvas?.drawCircle(it.x, it.y, radius.toFloat() - 11f, tempPaint)
            }
            tempPaint.color = it.color
            canvas?.drawCircle(it.x, it.y, radius.toFloat() - 16f, tempPaint)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean =
            when (event.action) {
                MotionEvent.ACTION_DOWN ->
                    true
                MotionEvent.ACTION_UP -> {
                    palette.forEach {
                        val diffX = event.x - it.x
                        val diffY = event.y - it.y
                        val distance = Math.sqrt((diffX.pow(2) + diffY.pow(2)).toDouble())
                        if (distance < radius) {
                            it.selected = true
                            colorChangeListener?.colorChanged(it.color)
                            invalidate()
                        } else {
                            it.selected = false
                        }
                    }
                    true
                }
                else -> super.onTouchEvent(event)
            }


    class PaletteColor(
            val color: Int,
            val x: Float,
            val y: Float,
            var selected: Boolean = false
    )
}

interface OnColorChangeListener {
    fun colorChanged(color: Int)

}
