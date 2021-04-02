package com.example.onescreenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.history.LoadLast10History
import com.example.onescreenapp.database.history.LoadLast30History
import com.example.onescreenapp.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)


        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true) // TODO ystroke
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        cartesian.yAxis(0).title("Pressure").minorLabels()
        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

        val set = Set.instantiate()
        set.data(loadData())

        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value2' }")

        val series1: Line = cartesian.line(series1Mapping)
        series1.name("Upper")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        val series2: Line = cartesian.line(series2Mapping)
        series2.name("Lower")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)

        binding.anyChartView.setChart(cartesian)
    }

    private fun loadData(): MutableList<DataEntry> {
        val instance = AppDatabase.getInstance(this)

        return LoadLast30History().execute(instance)
            .get()
            .map { CustomDataEntry(it.getDateString(), it.upperPressure, it.lowerPressure) }
            .toMutableList()

    }

    private class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
        }
    }
}