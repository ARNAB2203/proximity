package com.assignment.airquality.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assignment.airquality.repo.model.AirQuaData

@Dao
interface AirQuaDao {

    @Query("SELECT id, city, aqi, max(timestamp) as 'timestamp' FROM air_qua_table GROUP BY city ORDER BY city ASC")
    fun getUpdatedData(): LiveData<List<AirQuaData>>

    @Query("SELECT * FROM air_qua_table WHERE city LIKE :cityName ORDER BY id ASC")
    fun getEarlierByCityName(cityName: String): LiveData<List<AirQuaData>>

    @Query("SELECT * FROM air_qua_table WHERE city LIKE :cityName ORDER BY id DESC LIMIT 1")
    fun getLatestByCityName(cityName: String): LiveData<AirQuaData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(airQuaData: List<AirQuaData>): List<Long>
}
