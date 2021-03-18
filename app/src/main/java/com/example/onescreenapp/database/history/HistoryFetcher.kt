package com.example.onescreenapp.database.history

import android.os.AsyncTask
import com.example.onescreenapp.database.AppDatabase
import com.example.onescreenapp.database.entity.Pressure

class LoadLast10History : AsyncTask<AppDatabase, Unit, List<Pressure>>() {
    override fun doInBackground(vararg p0: AppDatabase?): List<Pressure>? {
        return p0[0]?.pressureDao()
            ?.getLast10()
            ?.sortedByDescending { x -> x.date }
            ?.toList()
    }
}

class LoadHistory : AsyncTask<AppDatabase, Unit, List<Pressure>>() {
    override fun doInBackground(vararg p0: AppDatabase?): List<Pressure>? {
        return p0[0]?.pressureDao()
            ?.getAllOrderByDate()
            ?.toList()
    }
}