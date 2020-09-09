package com.example.onescreenapp

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val pressureList = mutableListOf<Pressure>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val instance = AppDatabase.getInstance(this)

        upper.minValue = 90
        upper.maxValue = 190
        upper.value = 120


        lower.minValue = 70
        lower.maxValue = 110
        lower.value = 80

        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            R.layout.mytextview,
            pressureList
        )
        pressureListCtrl.adapter = adapter;

        AsyncTask.execute {
            instance?.pressureDao()
                ?.getAll()
                ?.sortedByDescending { x -> x.date }
                ?.let { pressureList.addAll(it) }
            adapter.notifyDataSetChanged();
        }

        diceRollerBtn.setOnClickListener {
            val diceRoller = Intent(this, DiceRoller::class.java)
            startActivity(diceRoller)
        }

        confirmBtn.setOnClickListener {
            val upperPressure: Int = upper.value
            val lowerPressure: Int = lower.value
            val pressureResult =
                Pressure(
                    upperPressure = upperPressure,
                    lowerPressure = lowerPressure,
                    date = Date()
                )
            info.text = if (pressureResult.correct()) {
                AsyncTask.execute {
                    instance?.runInTransaction {
                        instance.pressureDao().insertAll(pressureResult)
                    }
                }
                pressureList.add(pressureResult)
                pressureResult.getSummary()
            } else {
                "Niepoprawne dane"
            }
        }
    }
}