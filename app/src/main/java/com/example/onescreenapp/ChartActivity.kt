package com.example.onescreenapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.database.history.LoadLast30History
import com.example.onescreenapp.databinding.ActivityChartBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition.INSIDE_CHART
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*


class ChartActivity : AppCompatActivity() {

    private var seekBarX: SeekBar? = null;
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null;
    private var tvY: TextView? = null
    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        tvX = binding.tvXMax
//        tvY = binding.tvYMax

        seekBarX = binding.seekBar1.apply {
            progress = 45
        }

        val chart: LineChart = binding.chart1.apply {
            // no description text
            description.isEnabled = false

            // enable touch gestures
            setTouchEnabled(true)

            dragDecelerationFrictionCoef = 0.9f

            // enable scaling and dragging
            isDragEnabled = true
            setScaleEnabled(true)
            setDrawGridBackground(false)
            isHighlightPerDragEnabled = true
            // set an alternative background color
            setBackgroundColor(Color.WHITE)
            setViewPortOffsets(10f, 10f, 20f, 20f)
            // add data
            this.loadData()

            axisLeft.apply {
                setPosition(INSIDE_CHART)
                textColor = ColorTemplate.getHoloBlue()
                setDrawGridLines(true)
                isGranularityEnabled = true
                axisMinimum = 50f
                axisMaximum = 180f
                yOffset = 10f
                textColor = Color.rgb(255, 192, 56)
            }
            axisRight.apply {
                isEnabled = false
            }
        }

        // add data
        seekBarX!!.progress = 100

        // get the legend (only possible after setting data)

        // get the legend (only possible after setting data)
        chart.legend.apply {
            isEnabled = true
            this.xOffset = 10f
            this.yOffset = 200f
        }

        chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM_INSIDE
        typeface = Typeface.MONOSPACE
            textSize = 10f
            textColor = Color.WHITE
            setDrawAxisLine(false)
            setDrawGridLines(true)
            textColor = Color.rgb(255, 192, 56)
            setCenterAxisLabels(true)
            granularity = 24f // one hour
            xOffset = 100f

            valueFormatter = valueFormatter()
        }
    }

    private fun valueFormatter() = object : ValueFormatter() {
        private val mFormat = SimpleDateFormat("HH:mm MM-dd", Locale.ENGLISH)
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return mFormat.format(value)
        }

        override fun getFormattedValue(value: Float, axis: AxisBase): String {
            return mFormat.format(value)
        }
    }

    private fun LineChart.loadData() {

        val pressureData = loadDatabase()

        val upper: List<Entry> = pressureData.map { entry(it.date, it.upperPressure) }.toList()
        val lower: List<Entry> = pressureData.map { entry(it.date, it.lowerPressure) }.toList()

        // create a dataset and give it a type
        val set1 = LineDataSet(upper, "Upper").apply {
            axisDependency = AxisDependency.LEFT
            color = ColorTemplate.rgb("#015cdf")
            valueTextColor = ColorTemplate.getHoloBlue()
            lineWidth = 1.5f
            setDrawCircles(false)
            setDrawValues(true)
            fillAlpha = 65
            fillColor = ColorTemplate.rgb("#015cdf")
            highLightColor = Color.rgb(1, 0, 210)
            setDrawCircleHole(false)
        }

        val set2 = LineDataSet(lower, "Lower").apply {
            axisDependency = AxisDependency.LEFT
            color = ColorTemplate.rgb("#01b958")
            valueTextColor = ColorTemplate.getHoloBlue()
            lineWidth = 1.5f
            setDrawCircles(false)
            setDrawValues(true)
            fillAlpha = 65
            fillColor = ColorTemplate.rgb("#01b958")
            highLightColor = Color.rgb(1, 0, 210)
            setDrawCircleHole(false)
        }
        // create a data object with the data sets
        val data = LineData(set1, set2)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)

        // set data
        this.data = data
    }

    private fun entry(date: Date, pressure: Int): Entry {
        val time = date.time.toFloat()
        val press = pressure.toFloat()
        return Entry(time, press)
    }

    private fun loadDatabase(): List<Pressure> {
        val instance = AppDatabase.getInstance(this)

        return LoadLast30History().execute(instance)
            .get()

    }

}

