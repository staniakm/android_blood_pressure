package com.example.onescreenapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onescreenapp.database.entity.Pressure
import com.example.onescreenapp.database.dao.PressureDao

@Database(entities = [Pressure::class], version = 2)
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
                        ).addMigrations(MIGRATION_1_2)
                            .build()
                    }
                }
            }
            return sInstance
        }

        // Migration from 1 to 2, Room 2.1.0
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE Pressure ADD COLUMN pulse INTEGER NOT NULL DEFAULT 0")
            }
        }
    }

}
