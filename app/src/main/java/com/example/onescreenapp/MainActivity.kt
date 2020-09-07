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
        println("creating......")

        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            pressureList
        )
        pressureListCtrl.adapter = adapter;

        AsyncTask.execute {
            instance?.pressureDao()
                ?.getAll()
                ?.sortedByDescending { x-> x.date }
                ?.let { pressureList.addAll(it) }
            adapter.notifyDataSetChanged();
        }

        diceRollerBtn.setOnClickListener {
            val diceRoller = Intent(this, DiceRoller::class.java)
            startActivity(diceRoller)
        }

        confirmBtn.setOnClickListener {
            val upperPressure: Int = Integer.valueOf(upper.text.toString())
            val lowerPressure: Int = Integer.valueOf(lower.text.toString())
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