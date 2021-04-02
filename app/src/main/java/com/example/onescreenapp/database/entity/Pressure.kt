package com.example.onescreenapp.database.entity

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.onescreenapp.database.conterter.Converters
import java.text.SimpleDateFormat
import java.util.*

@Entity
@TypeConverters(Converters::class)
data class Pressure(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "first_name") val upperPressure: Int,
    @ColumnInfo(name = "last_name") val lowerPressure: Int,
    @ColumnInfo(name = "date") val date: Date
) {
    fun isValid() = upperPressure > 100 && lowerPressure > 60

    fun getSummary() = """Skurczowe: ${calculateUpper(upperPressure)}
                |rozkurczowe: ${calculateLower(lowerPressure)}""".trimMargin()

    @SuppressLint("SimpleDateFormat")
    fun getDateString(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return simpleDateFormat.format(date)
    }

    companion object {

        fun calculateLower(lowerPressure: Int): String {
            return when (lowerPressure) {
                in 0..79 -> "Za niskie"
                in 80..84 -> "Prawidłowe (80-84)"
                in 85..89 -> "Wysokie prawidłowe (85-89)"
                in 90..99 -> "Nadciśnienie 1 stopnia (90-99)"
                in 100..109 -> "Nadcisnienie 2 stopnia (100-109)"
                else -> "Nadcisnienie 3 stopnia (>= 110)"
            }
        }

        fun calculateUpper(upperPressure: Int): String {
            return when (upperPressure) {
                in 0..120 -> "Za niskie"
                in 120..129 -> "Prawidłowe (120-129)"
                in 130..139 -> "Wysokie prawidłowe (130-139)"
                in 140..159 -> "Nadciśnienie 1 stopnia(140-159)"
                in 160..179 -> "Nadcisnienie 2 stopnia (160-179)"
                else -> "Nadcisnienie 3 stopnia (>= 180)"
            }
        }
    }

    override fun toString(): String {
        return """
                |${DateFormat.format("yyyy-MM-dd HH:mm:ss", date)}
                |skurczowe=$upperPressure (${calculateUpper(upperPressure)})
                |rozkurczowe=$lowerPressure (${calculateLower(lowerPressure)}
                |""".trimMargin()
    }
}
