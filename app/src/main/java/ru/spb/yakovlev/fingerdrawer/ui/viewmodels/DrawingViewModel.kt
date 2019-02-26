package ru.spb.yakovlev.fingerdrawer.ui.viewmodels

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DrawingViewModel : ViewModel() {

    var cachedBitmapWithOrientation: Pair<Bitmap, Int>? = null

    val currentRotationLive = MutableLiveData<Int>()

    var currentRotation: Int = 0
        set(value) {
            val oldValue = field
            val newValue = when (value) {
                in 45..135 -> 90
                in 135..225 -> 180
                in 225..315 -> 270
                else -> 0
            }
            if (oldValue != newValue) {
                currentRotationLive.postValue(newValue)
                field = newValue
            }
        }



    val currentRotationNormalized: Int
        get() = when (currentRotation) {
            in 45..135 -> 90
            in 135..225 -> 180
            in 225..315 -> 270
            else -> 0
        }


    var measuredWidth = 0
    var measuredHeight = 0

    val matrix = Matrix()


    val paths = mutableListOf(Pair(Path(), newPaint()))
    var currentPath = Pair(Path(), newPaint())

    fun getMatrix(sourceAngle: Int): Matrix {
        val rotationAngle = ((360 + currentRotationNormalized - sourceAngle) % 360).toFloat()
        matrix.reset()
        matrix.postRotate(rotationAngle)

        return matrix
    }

    fun setupModelAfterRotation() {

        paths.clear()
        setColor(currentPath.second.color)
    }

//    fun getAngleToRotateScreen() {
//        when (rotation) {
//            android.view.Surface.ROTATION_0 -> {
//                Log.d("1234567899DrVM", " 0")
//            }
//            android.view.Surface.ROTATION_90 -> {
//                Log.d("1234567899DrVM", " 90")
//            }
//            android.view.Surface.ROTATION_180 -> {
//                Log.d("1234567899DrVM", " 180")
//            }
//            android.view.Surface.ROTATION_270 -> {
//                Log.d("1234567899DrVM", " 270")
//            }
//        }
//    }


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
