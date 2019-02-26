package ru.spb.yakovlev.fingerdrawer.ui.mainactivity

import android.graphics.Color
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main_fragment.*
import ru.spb.yakovlev.fingerdrawer.R
import ru.spb.yakovlev.fingerdrawer.ui.viewmodels.DrawingViewModel
import ru.spb.yakovlev.fingerdrawer.ui.viewmodels.PaletteViewModel

class MainActivityFragment : Fragment() {

    val INIT_LINE_WIDTH = 50
    val INIT_COLOR = Color.BLACK
    val INIT_BACKGROUND_COLOR = Color.GREEN

    companion object {
        fun newInstance() = MainActivityFragment()
    }

    private lateinit var orientationEventListener: OrientationEventListener
    private lateinit var drawingViewModel: DrawingViewModel
    private lateinit var paletteViewModel: PaletteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //paletteViewModel.clearPalette()
        Log.d("1234567899Fragment", "onCreateView")

        return inflater.inflate(R.layout.activity_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            drawingViewModel = ViewModelProviders.of(this).get(DrawingViewModel::class.java)
            paletteViewModel = ViewModelProviders.of(this).get(PaletteViewModel::class.java)

            drawing.model = drawingViewModel
            paletteViewModel.clearPalette()
            palette.model = paletteViewModel

        }

        palette.setOnColorChangeListener(object : OnColorChangeListener {
            override fun colorChanged(color: Int) {
                drawingViewModel.setColor(color)
                paletteViewModel.currentColor = color
            }
        })

        brashSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var strProcess = progress.toString()
                if (progress < 1) strProcess = "1"
                brashSize.text = strProcess
                drawingViewModel.setLineWidth(progress)
            }
        })

        orientationEventListener = object : OrientationEventListener(
            context,
            SensorManager.SENSOR_DELAY_NORMAL
        ) {
            override fun onOrientationChanged(orientation: Int) {
                Log.d("1234567899Activity", "onOrientationChanged to $orientation")
                drawingViewModel.currentRotation = orientation
            }

        }
        if (orientationEventListener.canDetectOrientation()) {
            Log.d("1234567899Activity", "Can detect orientation")
            orientationEventListener.enable()
        } else {
            Log.d("1234567899Activity", "Cannot detect orientation")
            orientationEventListener.disable()
        }

        drawingViewModel.currentRotationLive.observe(this, Observer {
            redrawScreenAfterRotate()
        })


    }

    fun redrawScreenAfterRotate() {
        drawingViewModel.setupModelAfterRotation()

        if (drawingViewModel.cachedBitmapWithOrientation != null) {
            drawing.loadBackground()

        }
    }


    override fun onResume() {
        super.onResume()
//        drawingViewModel.getAngleToRotateScreen()

        if (drawingViewModel.cachedBitmapWithOrientation == null) {
            drawing.setBackgroundColor(INIT_BACKGROUND_COLOR)
            //drawingViewModel.setLineWidth(INIT_LINE_WIDTH)
            brashSizeSeekBar.progress = INIT_LINE_WIDTH
            paletteViewModel.currentColor = INIT_COLOR

            Log.d("1234567899Fragment", "onResume Color.WHITE")
        } else {
            Log.d("1234567899Fragment", "onResume drawing.background ")

            //drawing.background = drawing.getDrawable(resources,drawingViewModel.cacheBitmap!!)
        }


        drawing.invalidate()
    }

//    override fun onPause() {
//        super.onPause()
//        Log.d("1234567899Fragment", "onPause")
//        drawingViewModel.cacheBitmap=drawing.getBitmap()
//        Log.d("1234567899Fragment", "onPause drawingViewModel.cacheBitmap is null = ${drawingViewModel.cacheBitmap==null}")
//        drawingViewModel.paths.clear()
//    }

//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        activity?.windowManager?.defaultDisplay?.run {
//            when (rotation){
//                android.view.Surface.ROTATION_0 -> {
//                    Log.d("1234567899DrVM"," 0+")
//                }
//                android.view.Surface.ROTATION_90 -> {
//                    Log.d("1234567899DrVM"," 90+")
//                }
//                android.view.Surface.ROTATION_180 -> {
//                    Log.d("1234567899DrVM"," 180+")
//                }
//                android.view.Surface.ROTATION_270 -> {
//                    Log.d("1234567899DrVM"," 270+")
//                }
//                else -> Log.d("1234567899DrVM"," Error")
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("1234567899Activity", "onDestroyView")
        orientationEventListener.disable()
    }
}
