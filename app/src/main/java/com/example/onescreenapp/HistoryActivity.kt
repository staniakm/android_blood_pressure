package com.example.onescreenapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onescreenapp.adapter.HistoryListAdapter
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.history.LoadHistory
import com.example.onescreenapp.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareView()
        loadData()
    }

    private fun prepareView() {
        binding.historyView
            .apply {
                layoutManager = LinearLayoutManager(this@HistoryActivity)
                historyAdapter = HistoryListAdapter()
                adapter = historyAdapter
            }
    }

    private fun loadData() {
        val instance = AppDatabase.getInstance(this)

        LoadHistory().execute(instance)
            .get()
            .let { historyAdapter.submitList(it) }
    }
}

