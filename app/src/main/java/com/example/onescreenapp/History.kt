package com.example.onescreenapp

import android.os.AsyncTask
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
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
        LoadHistory().execute(instance)
            .get()
            .let { pressureList.addAll(it) }


    }
}

class LoadHistory : AsyncTask<AppDatabase, Unit, List<Pressure>>() {
    override fun doInBackground(vararg p0: AppDatabase?): List<Pressure>? {
        return p0[0]?.pressureDao()
            ?.getAll()
            ?.sortedByDescending { x -> x.date }
            ?.toList()
    }

}