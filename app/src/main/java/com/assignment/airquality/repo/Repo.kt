package com.assignment.airquality.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.assignment.airquality.repo.dao.AirQuaDao
import com.assignment.airquality.repo.model.AirQuaData

@Database(entities = [AirQuaData::class], version = 1)
abstract class Repo : RoomDatabase() {

    companion object {

        @Volatile
        private var instance: Repo? = null

        @JvmStatic
        fun getInstance(context: Context): Repo = synchronized(this) {
            if (instance == null) instance = buildDatabase(context)
            return instance as Repo
        }

        private fun buildDatabase(context: Context): Repo = Room.databaseBuilder(
            context,
            Repo::class.java,
            "air_qua_db"
        ).build()
    }

    abstract fun airQuaDao(): AirQuaDao
}
