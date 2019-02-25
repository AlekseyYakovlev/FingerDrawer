package ru.spb.yakovlev.fingerdrawer.ui.mainactivity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import ru.spb.yakovlev.fingerdrawer.ui.viewmodels.PaletteViewModel
import kotlin.math.pow

class PaletteView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var colorChangeListener: OnColorChangeListener? = null
    lateinit var model: PaletteViewModel
    private val tempPaint = Paint()

    fun setOnColorChangeListener(changeListener: OnColorChangeListener) {
        colorChangeListener = changeListener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        model.run {
            setupRadius(measuredWidth)
            setupPalette()
            setMeasuredDimension(radius * palette.size * 2, radius * 2)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        model.run {
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
    }


    override fun onTouchEvent(event: MotionEvent): Boolean =
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                true
            }
            MotionEvent.ACTION_UP -> {
                model.run {
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
                }
                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }

}

interface OnColorChangeListener {
    fun colorChanged(color: Int)

}

