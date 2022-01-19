package com.assignment.airquality.repo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = "air_qua_table")
data class AirQuaData(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "city") val city: String,
    @ColumnInfo(name = "aqi") val aqi: Double,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Calendar.getInstance().timeInMillis
)
