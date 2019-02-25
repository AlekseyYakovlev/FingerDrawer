package ru.spb.yakovlev.fingerdrawer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.spb.yakovlev.fingerdrawer.R
import ru.spb.yakovlev.fingerdrawer.ui.mainactivity.MainActivityFragment
import ru.spb.yakovlev.fingerdrawer.ui.viewmodels.DrawingViewModel


class MainActivity : AppCompatActivity() {

    //    private lateinit var orientationEventListener:OrientationEventListener
    private lateinit var drawingViewModel: DrawingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_activity)

//        orientationEventListener = object : OrientationEventListener(
//            this,
//            SensorManager.SENSOR_DELAY_NORMAL
//        ){
//            override fun onOrientationChanged(orientation: Int) {
//                Log.d("1234567899Activity", "onOrientationChanged to $orientation")
//            }
//
//        }
//        if (orientationEventListener.canDetectOrientation()) {
//            Log.d("1234567899Activity", "Can detect orientation")
//            orientationEventListener.enable()
//        } else {
//            Log.d("1234567899Activity", "Cannot detect orientation")
//            orientationEventListener.disable()
//        }


//        drawingViewModel = ViewModelProviders.of(this).get(DrawingViewModel::class.java)
//
//        drawingViewModel.rotation = windowManager.defaultDisplay.rotation
//        drawingViewModel.getAngleToRotateScreen()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainActivityFragment.newInstance())
                .commitNow()
        }
    }
//
//    override fun onContentChanged() {
//        super.onContentChanged()
//        Log.d("1234567899Activity", "onContentChanged")
//    }
//
//    override fun onConfigurationChanged(newConfig: Configuration?) {
//        super.onConfigurationChanged(newConfig)
//        Log.d("1234567899Activity", "onConfigurationChanged")
//    }

//protected override fun onDestroy() {
//    super.onDestroy()
//    Log.d("1234567899Activity", "onDestroy")
//    orientationEventListener.disable()
//}

}
