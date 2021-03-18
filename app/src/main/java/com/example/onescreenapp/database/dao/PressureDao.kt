package com.example.onescreenapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.onescreenapp.database.entity.Pressure

@Dao
interface PressureDao {

    @Query("SELECT * FROM pressure order by date desc limit 10")
    fun getLast10(): List<Pressure>

    @Query("SELECT * FROM pressure order by date desc")
    fun getAllOrderByDate(): List<Pressure>

    @Query("SELECT * FROM pressure WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Pressure>

    @Insert
    fun insertAll(vararg users: Pressure)

    @Delete
    fun delete(user: Pressure)
}
