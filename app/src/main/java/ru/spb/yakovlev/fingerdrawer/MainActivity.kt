package ru.spb.yakovlev.fingerdrawer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        palette.setOnColorChangeListener(object : OnColorChangeListener {
            override fun colorChanged(color: Int) {
                drawing.setColor(color)
            }
        })


        brashSize.text = "1"
        brashSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                var strProcess = progress.toString()
                if (progress < 1) strProcess = "1"
                brashSize.text = strProcess
                drawing.setLineWidth(progress)
            }
        })
    }
}
