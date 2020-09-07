package com.example.onescreenapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
