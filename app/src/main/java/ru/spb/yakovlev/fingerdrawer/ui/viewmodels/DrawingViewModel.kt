package ru.spb.yakovlev.fingerdrawer.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {


    var angleToRotate: Float = 0f
    var previousRotation: Int = 0

    var cacheBitmap: Bitmap? = null
    var cachedBitmapWithOrientation: Pair<Bitmap, Int>? = null

    var currentRotation: Int = 0

    val currentRotationNormalized: Int
        get() = when (currentRotation) {
            in 45..135 -> 90
            in 135..225 -> 180
            in 225..315 -> 270
            else -> 0
        }

    var rotation: Int = 0
        set(value) {
            field = value
            angleToRotate = (((4 - value + previousRotation) % 4) * 90).toFloat()
        }

    var centerX = 0f
    var centerY = 0f
//    var rotationPoint = 0f

    var measuredWidth = 0
    var measuredHeight = 0

    val matrix = Matrix()
//        get() {
//            field.reset()
//            field.postRotate(angleToRotate)
//            return field
//        }

    val paths = mutableListOf(Pair(Path(), newPaint()))
    var currentPath = Pair(Path(), newPaint())

    fun getAngleToRotateScreen() {
        when (rotation) {
            android.view.Surface.ROTATION_0 -> {
                Log.d("1234567899DrVM", " 0")
            }
            android.view.Surface.ROTATION_90 -> {
                Log.d("1234567899DrVM", " 90")
            }
            android.view.Surface.ROTATION_180 -> {
                Log.d("1234567899DrVM", " 180")
            }
            android.view.Surface.ROTATION_270 -> {
                Log.d("1234567899DrVM", " 270")
            }
        }
    }

    private fun addPathWithNewAttribute(attribute: String, value: Int) {
        val paint = newPaint()

        when (attribute) {
            "color" -> {
                paint.color = value
                paint.strokeWidth = paths.lastOrNull()?.second?.strokeWidth
                    ?: currentPath.second.strokeWidth
            }
            "width" -> {
                paint.strokeWidth = value.toFloat()
                paint.color = paths.lastOrNull()?.second?.color
                    ?: currentPath.second.color
            }
        }
        currentPath = Path() to paint
        paths.add(currentPath)
    }

    fun setColor(color: Int) {
        addPathWithNewAttribute("color", color)
    }

    fun setLineWidth(width: Int) {
        addPathWithNewAttribute("width", width)
    }

    fun newPaint(color: Int = -0x1000000, width: Int = 10): Paint {
        val paint = Paint()
        with(paint) {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = width.toFloat()
        }
        return paint
    }


}
