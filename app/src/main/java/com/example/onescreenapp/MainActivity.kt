package com.example.onescreenapp

import android.app.AlertDialog
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

    private val pressureList = mutableListOf<Pressure>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val instance = AppDatabase.getInstance(this)
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            R.layout.mytextview,
            pressureList
        )
        pressureListCtrl.adapter = adapter

        AsyncTask.execute {
            instance?.pressureDao()
                ?.getAll()
                ?.sortedByDescending { x -> x.date }
                ?.let { pressureList.addAll(it) }
            adapter.notifyDataSetChanged()
        }

        setUpperValues()
        setLowerValues()

        diceRollerBtn.setOnClickListener {
            val diceRoller = Intent(this, DiceRoller::class.java)
            startActivity(diceRoller)
        }

        confirmBtn.setOnClickListener {
            val pressureResult =
                Pressure(
                    upperPressure = upper.value,
                    lowerPressure = lower.value,
                    date = Date()
                )
            val result = if (pressureResult.correct()) {
                savePressureResult(instance, pressureResult)
                pressureList.add(pressureResult)
                pressureList.sortByDescending { x -> x.date }

                adapter.notifyDataSetChanged()
                pressureResult.getSummary()

            } else {
                "Niepoprawne dane"
            }

            showResultDialog(result)
        }
    }

    private fun savePressureResult(instance: AppDatabase?, pressureResult: Pressure) {
        AsyncTask.execute {
            instance?.runInTransaction {
                instance.pressureDao().insertAll(pressureResult)
            }
        }
    }

    private fun showResultDialog(result: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("Wynik")
        alertDialog.setMessage(result)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL,
            "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun setLowerValues() {
        lower.minValue = 70
        lower.maxValue = 110
        lower.value = 80
    }

    private fun setUpperValues() {
        upper.minValue = 90
        upper.maxValue = 190
        upper.value = 120
    }
}