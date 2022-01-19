package com.assignment.airquality.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.assignment.airquality.repo.Repo
import com.assignment.airquality.repo.model.AirQuaData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class ProximityViewModel(private val repo: Repo) : ViewModel()  {

    fun parseMessage(message: String): List<AirQuaData> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, AirQuaData::class.java)
        val adapter: JsonAdapter<List<AirQuaData>> = moshi.adapter(type)
        return adapter.fromJson(message) as List<AirQuaData>
    }



    fun insertData(list: List<AirQuaData>) = repo.airQuaDao().insertAll(list)
    fun getUpdatedData(): LiveData<List<AirQuaData>> = repo.airQuaDao().getUpdatedData()
    fun getEarlierByCityName(cityName: String): LiveData<List<AirQuaData>> = repo.airQuaDao().getEarlierByCityName(cityName)
    fun getLatestByCityName(cityName: String): LiveData<AirQuaData> = repo.airQuaDao().getLatestByCityName(cityName)
}