package com.example.onescreenapp

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.NumberPicker
import androidx.appcompat.app.AppCompatActivity
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpperValues()
        setLowerValues()

        history.setOnClickListener {
            openHistory()
        }
    }

    private fun openHistory() {
        val history = Intent(this, History::class.java)
        startActivity(history)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save_btn){
            val instance = AppDatabase.getInstance(this)

            val pressureResult =
                Pressure(
                    upperPressure = upper.value,
                    lowerPressure = lower.value,
                    date = Date()
                )
            val result = if (pressureResult.correct()) {
                savePressureResult(instance, pressureResult)
                pressureResult.getSummary()

            } else {
                "Niepoprawne dane"
            }

            showResultDialog(result)
        }
        return super.onOptionsItemSelected(item)
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