package com.assignment.airquality.citylist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.airquality.R
import com.assignment.airquality.getAQI
import com.assignment.airquality.getColorCode
import com.assignment.airquality.getTime
import com.assignment.airquality.repo.model.AirQuaData

class AirQuaDataAdapter(
    private val cityList: List<AirQuaData>,
    private val listener: AirQuaListener
) : RecyclerView.Adapter<AirQuaDataAdapter.CityAQIViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityAQIViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_airqua_item, parent, false)
        return CityAQIViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: CityAQIViewHolder, position: Int) {
        holder.bind(cityList[position])
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    class CityAQIViewHolder(
        itemView: View,
        private val listener: AirQuaListener
    ) : RecyclerView.ViewHolder(itemView) {

        private val cityItemLayout: LinearLayout = itemView.findViewById(R.id.city_item_layout)
        private val cityNameTextView: TextView = itemView.findViewById(R.id.city_name)
        private val currentAQITextView: TextView = itemView.findViewById(R.id.current_aqi)
        private val lastUpdatedTextView: TextView = itemView.findViewById(R.id.last_updated)

        fun bind(cityAQI: AirQuaData) {
//            cityItemLayout.setBackgroundColor(cityAQI.aqi.getColorCode(itemView.context))

            cityNameTextView.text = cityAQI.city
            currentAQITextView.text = cityAQI.aqi.getAQI()
            currentAQITextView.setTextColor(cityAQI.aqi.getColorCode(itemView.context))
            lastUpdatedTextView.text = cityAQI.timestamp.getTime()

            itemView.setOnClickListener { listener.onClicked(cityAQI.city) }
        }
    }
}

interface AirQuaListener {
    fun onClicked(details: String)
}
