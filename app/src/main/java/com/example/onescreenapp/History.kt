package com.example.onescreenapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure
import kotlinx.android.synthetic.main.activity_history.*

class History : AppCompatActivity() {
    private val pressureList = mutableListOf<Pressure>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)


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
    }
}