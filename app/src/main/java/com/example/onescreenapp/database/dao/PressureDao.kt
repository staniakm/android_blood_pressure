package com.example.onescreenapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.onescreenapp.database.entity.Pressure

@Dao
interface PressureDao {
    @Query("SELECT * FROM pressure")
    fun getAll(): List<Pressure>

    @Query("SELECT * FROM pressure WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Pressure>

    @Insert
    fun insertAll(vararg users: Pressure)

    @Delete
    fun delete(user: Pressure)
}
