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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onescreenapp.adapter.HistoryListAdapter
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.database.history.LoadLast10History
import com.example.onescreenapp.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var historyAdapter: HistoryListAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpperValues()
        setLowerValues()
        setPulseValues()
        binding.saveBtn.setOnClickListener {
            saveResult()
        }

        binding.lower.setOnValueChangedListener { _, _, i2 ->
            binding.lowwerSummary.text = """Skurczowe: ${Pressure.calculateLower(i2)}"""
        }

        binding.upper.setOnValueChangedListener { _, _, i2 ->
            binding.upperSummary.text = "Rozkurczowe: ${Pressure.calculateUpper(i2)}"
        }
    }

    override fun onStart() {
        super.onStart()
        loadHistory()
    }

    private fun loadHistory() {
        prepareView()
        loadData()
    }

    private fun prepareView() {
        binding.historyRecycleView
            .apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                historyAdapter = HistoryListAdapter()
                adapter = historyAdapter
            }
    }

    private fun loadData() {
        val instance = AppDatabase.getInstance(this)

        LoadLast10History().execute(instance)
            .get()
            .let { historyAdapter.submitList(it) }
    }

    private fun saveResult() {
        val pressureResult =
            Pressure(
                upperPressure = binding.upper.value,
                lowerPressure = binding.lower.value,
                pulse = binding.pulse.value,
                date = Date()
            )
        val result = if (pressureResult.isValid()) {
            savePressureResult(AppDatabase.getInstance(this), pressureResult)
        } else {
            "Niepoprawne dane"
        }

        showResultDialog(result)
        loadHistory()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.history_menu_btn) {
            startActivity(Intent(this, HistoryActivity::class.java))
        } else if (item.itemId == R.id.chart_menu_btn) {
            startActivity(Intent(this, ChartActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePressureResult(instance: AppDatabase?, pressureResult: Pressure): String {
        AsyncTask.execute {
            instance?.runInTransaction {
                instance.pressureDao().insertAll(pressureResult)
            }
        }
        return pressureResult.getSummary()
    }

    private fun showResultDialog(result: String) {
        val alertDialog: AlertDialog = AlertDialog.Builder(this@MainActivity).create()
        alertDialog.setTitle("Wynik")
        alertDialog.setMessage(result)
        alertDialog.setButton(BUTTON_NEUTRAL, "OK") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun setLowerValues() {
        binding.lower.minValue = 60
        binding.lower.maxValue = 110
        binding.lower.value = 80
    }

    private fun setUpperValues() {
        binding.upper.minValue = 90
        binding.upper.maxValue = 190
        binding.upper.value = 120
    }

    private fun setPulseValues() {
        binding.pulse.minValue = 40
        binding.pulse.maxValue = 120
        binding.pulse.value = 70
    }
}

