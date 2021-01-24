package com.example.onescreenapp

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.database.history.LoadHistory
import com.example.onescreenapp.databinding.ActivityHistoryBinding

class History : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    private val pressureList = mutableListOf<Pressure>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val instance = AppDatabase.getInstance(this)
        val adapter: ArrayAdapter<*> = ArrayAdapter(
            this,
            R.layout.mytextview,
            pressureList
        )
        binding.pressureListCtrl.adapter = adapter
        LoadHistory().execute(instance)
            .get()
            .let { pressureList.addAll(it) }
    }
}

