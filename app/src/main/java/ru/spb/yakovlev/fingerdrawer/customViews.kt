package ru.spb.yakovlev.fingerdrawer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class DrawingView(
        context: Context?,
        attrs: AttributeSet?
) : View(context, attrs) {

    val path = Path()
    val paths = mutableListOf(Pair(path, Paint()))

    fun newPaint(color: Int = 0, width:Int = 10): Paint {
        val paint = Paint()
        with(paint) {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = width.toFloat()
        }
        return paint

    }

    fun setColor(color: Int) {
        paths.lastOrNull()?.let {
            paths.add(Pair(Path(), newPaint(color,it.second.strokeWidth.toInt())))
            return
        }
        paths.add(Pair(Path(), newPaint()))
    }

    fun setLineWidth(width: Int) {
        paths.lastOrNull()?.let {
            paths.add(Pair(Path(), newPaint(it.second.color,width)))
            return
        }
        paths.add(Pair(Path(), newPaint()))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paths.forEach { canvas?.drawPath(it.first, it.second) }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return false
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                paths.last().first.moveTo(x, y)
            MotionEvent.ACTION_MOVE ->
                paths.last().first.lineTo(x, y)
            else ->
                return false
        }
        invalidate()
        return true
    }
}

