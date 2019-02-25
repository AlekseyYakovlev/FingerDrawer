package ru.spb.yakovlev.fingerdrawer.ui.mainactivity

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.drawToBitmap
import ru.spb.yakovlev.fingerdrawer.ui.viewmodels.DrawingViewModel


class DrawingView(
    context: Context?,
    attrs: AttributeSet?
) : View(context, attrs) {

    lateinit var model: DrawingViewModel

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

//        model.centerX = (measuredWidth / 2).toFloat()
//        model.centerY = (measuredHeight / 2).toFloat()
        model.measuredWidth = measuredWidth
        model.measuredHeight = measuredHeight

//        model.rotationPoint = (Math.min(measuredWidth, measuredHeight) / 2).toFloat()

        invalidate()


    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        model.paths.forEach { canvas?.drawPath(it.first, it.second) }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y
        val path = model.paths.lastOrNull()?.first

        when (event.action) {
            MotionEvent.ACTION_DOWN ->
                path?.moveTo(x, y)
            MotionEvent.ACTION_MOVE ->
                path?.lineTo(x, y)
            MotionEvent.ACTION_UP ->
                model.cachedBitmapWithOrientation = getBitmap() to model.currentRotationNormalized
            else ->
                return false
        }
        invalidate()

        return true
    }

//    fun getBitmap(): Bitmap {
//        //measure(MeasureSpec.UNSPECIFIED,MeasureSpec.UNSPECIFIED)
//        val bitmap = Bitmap.createBitmap(model.measuredWidth, model.measuredHeight, Bitmap.Config.ARGB_8888)
//        Log.d(
//            "1234567899DrawingView",
//            "model.measuredWidth ${model.measuredWidth}, model.measuredHeight ${model.measuredHeight}"
//        )
//        val canvas = Canvas(bitmap)
//
//        draw(canvas)
//        return bitmap
//    }

    fun getBitmap() = drawToBitmap()




    fun getDrawable(res: Resources, source: Bitmap): Drawable {

        //val source = getBitmap()
        val rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.width, source.height, model.matrix, true)
        return BitmapDrawable(res, rotatedBitmap)
    }

    fun getDrawable(res: Resources, source: Pair<Bitmap, Int>, currentOrientation: Int): Drawable {

        val rotationAngle = ((360 + model.currentRotationNormalized - source.second) % 360).toFloat()
        model.matrix.reset()
        model.matrix.postRotate(rotationAngle)

        val rotatedBitmap =
            Bitmap.createBitmap(source.first, 0, 0, source.first.width, source.first.height, model.matrix, true)
        return BitmapDrawable(res, rotatedBitmap)
    }

    fun loadBackground(orientation: Int) {
        if (model.cachedBitmapWithOrientation != null) {
            background = getDrawable(resources, model.cachedBitmapWithOrientation!!, orientation)
        }
    }
}

