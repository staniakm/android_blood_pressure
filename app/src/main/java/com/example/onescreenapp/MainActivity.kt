package com.example.onescreenapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.AlertDialog.BUTTON_NEUTRAL
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpperValues()
        setLowerValues()
        binding.history.setOnClickListener {
            openHistory()
        }

        binding.lower.setOnValueChangedListener { _, _, i2 ->
            binding.lowwerSummary.text = """Skurczowe: ${Pressure.calculateLower(i2)}"""
        }

        binding.upper.setOnValueChangedListener { _, _, i2 ->
            binding.upperSummary.text = "Rozkurczowe: ${Pressure.calculateUpper(i2)}"
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

        if (item.itemId == R.id.save_btn) {
            val instance = AppDatabase.getInstance(this)

            val pressureResult =
                Pressure(
                    upperPressure = binding.upper.value,
                    lowerPressure = binding.lower.value,
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
        alertDialog.setButton(BUTTON_NEUTRAL, "OK") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun setLowerValues() {
        binding.lower.minValue = 70
        binding.lower.maxValue = 110
        binding.lower.value = 80
    }

    private fun setUpperValues() {
        binding.upper.minValue = 90
        binding.upper.maxValue = 190
        binding.upper.value = 120
    }
}