package com.suvdev.pinball

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var coefficient = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Submit.setOnClickListener{
            getChartData()
        }
        initSeekBar()
    }

    private fun initSeekBar(){
        seekbar.setOnSeekBarChangeListener(object :OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.e(TAG, "Progress: "+ progress)
                coefficient = progress.toDouble()/10
                getChartData()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }


    private fun getChartData(){
        var height_start = 0.0
        val myEntries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        var i = 2f

        if(!et1.text.isNullOrEmpty()){
            height_start = et1.text.toString().toDouble()
            var time_fall = Math.sqrt(2*height_start/9.8)
            var total_time = time_fall

            myEntries.add(Entry(0f,height_start.toFloat()))
            labels.add(DecimalFormat("#.#").format(total_time).toString())
            myEntries.add(Entry(1f,0f))
            Log.e(TAG, "Coefficient: "+ coefficient)
            while (height_start>0.01){
                val height_bounce = coefficient*coefficient* height_start
                if(height_start-height_bounce<0.1)
                    break

                val time_bounce = 2* Math.sqrt(2*height_bounce/9.8)
                total_time += time_bounce
                labels.add(DecimalFormat("#.#").format(total_time).toString())

                height_start = height_bounce
                time_fall = Math.sqrt(2*height_start/9.8)
                total_time += time_fall
                labels.add(DecimalFormat("#.#").format(total_time).toString())

                myEntries.add(Entry(i,height_bounce.toFloat()))
                i++
                myEntries.add(Entry(i,0f))
                i++
                Log.e(TAG, "Entries size: "+ height_bounce)
            }
        }
        num_bounces.text = "Total Bounces: "+ myEntries.size
        val dataset = LineDataSet(myEntries, "PinBall")
        dataset.valueTextSize = 10f
        dataset.setDrawValues(false)
        dataset.lineWidth = 2f
        dataset.setDrawCircleHole(false)
        dataset.circleRadius = 4f
        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER)
        val data = LineData()
        data.addDataSet(dataset)

        chart.data = data
        chart.setDrawMarkers(false)
        chart.axisLeft.isEnabled = false
        chart.axisRight.isEnabled = false
        chart.description.setEnabled(false)
        chart.extraBottomOffset = 25f
        chart.legend.isEnabled = false
        chart.setScaleEnabled(true)
        chart.setScaleMinima(2f, 0f)
//            chart.setPinchZoom(true)
        chart.getViewPortHandler().setMaximumScaleX(2f)
        chart.getViewPortHandler().setMaximumScaleY(2f)
        if (labels != null)
            chart.marker = BuLineChartMarkerView(this, R.layout.single_marker_chart, ArrayList<String>(labels))

        val axis = chart.xAxis
        axis.setPosition(XAxis.XAxisPosition.BOTTOM)
        axis.textSize = 8f
        axis.valueFormatter = IndexAxisValueFormatter(labels)
        axis.labelRotationAngle = -45f
        chart.invalidate()
    }
}
