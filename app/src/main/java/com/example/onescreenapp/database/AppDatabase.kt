package com.example.onescreenapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.database.dao.PressureDao

@Database(entities = [Pressure::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pressureDao(): PressureDao

    companion object {
        private var sInstance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "database-name"
                        ).build()
                    }
                }
            }
            return sInstance
        }
    }
}
