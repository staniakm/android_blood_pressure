package com.example.onescreenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.history.LoadLast30History
import com.example.onescreenapp.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cartesian = getCartesian()

        val set = Set.instantiate()
        set.data(loadData())

        cartesian.line(set.mapAs("{ x: 'x', value: 'upper' }"))
            .apply {
                name("Upper")
                hovered().markers().enabled(true)
                hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4.0)
                tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5.0)
                    .offsetY(5.0)
            }

        cartesian.line(set.mapAs("{ x: 'x', value: 'lower' }"))
            .apply {
                name("Lower")
                hovered().markers().enabled(true)
                hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4.0)
                tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5.0)
                    .offsetY(5.0)
            }



        binding.anyChartView.setChart(cartesian)
    }

    private fun getCartesian(): Cartesian {
        return AnyChart.line().apply {
            animation(true)
            padding(10.0, 20.0, 5.0, 20.0)

            crosshair().enabled(true)
            crosshair()
                .yLabel(true) // TODO ystroke
                .yStroke(null as Stroke?, null, null, null as String?, null as String?)

            tooltip().positionMode(TooltipPositionMode.POINT)

            yAxis(0).title("Pressure").minorLabels()
            xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

            legend().enabled(true)
            legend().fontSize(13.0)
            legend().padding(0.0, 0.0, 10.0, 0.0)
        }
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
        upper: Number?,
        lower: Number?
    ) :
        ValueDataEntry(x, upper) {
        init {
            setValue("lower", lower)
        }
    }
}