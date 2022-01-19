package com.assignment.airquality.graph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.assignment.airquality.MainActivity
import com.assignment.airquality.R
import com.assignment.airquality.citylist.adapter.AirQuaDataAdapter
import com.assignment.airquality.citylist.adapter.AirQuaListener
import com.assignment.airquality.getSeconds
import com.assignment.airquality.repo.Repo
import com.assignment.airquality.repo.model.AirQuaData
import com.assignment.airquality.viewmodel.ProximityViewModel
import com.assignment.airquality.viewmodel.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_details_air_qua.*
import kotlin.math.roundToInt

class DetailsAirQuaFragment : Fragment(), AirQuaListener {
    private var viewModel: ProximityViewModel? = null
    private var listLiveData: LiveData<AirQuaData>? = null

    private var lastUpdatedTime: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_air_qua, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = initViewModel()
    }

    private fun initViewModel(): ProximityViewModel {
        val model: ProximityViewModel by viewModels { ViewModelFactory(Repo.getInstance(requireContext())) }
        return model
    }

    fun updateCity(cityName: String) {
        fetch(cityName = cityName)
        city_name.text = cityName

        listLiveData = viewModel?.getLatestByCityName(cityName)
        listLiveData?.observe(this, { fetch(it, cityName) })
    }

    private fun fetch(data: AirQuaData? = null, cityName: String) {
        if (lastUpdatedTime == 0L) {
            viewModel?.getEarlierByCityName(cityName)?.observe(this, { dataList ->
                viewModel?.getEarlierByCityName(cityName)?.removeObservers(this@DetailsAirQuaFragment)
                update(getEntries(dataList))
            })
        } else data?.let { update(getEntries(listOf(it))) }
    }

    private fun update(entries: List<Entry>) {
        val lineDataSet: LineDataSet
        if (chart_view.data != null && chart_view.data.dataSetCount > 0) {
            lineDataSet = chart_view.data.getDataSetByIndex(0) as LineDataSet
            lineDataSet.values = entries

            chart_view.data.notifyDataChanged()
            chart_view.notifyDataSetChanged()
        } else {
            lineDataSet = LineDataSet(entries, "Air Quality Index")
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
            lineDataSet.lineWidth = 1.8f
            lineDataSet.circleRadius = 1f
            lineDataSet.setDrawFilled(true)

            val dataSets: MutableList<ILineDataSet> = mutableListOf()
            dataSets.add(lineDataSet)

            val data = LineData(dataSets)
            chart_view.data = data
            chart_view.description.isEnabled = false
        }
        chart_view.invalidate()
    }

    private fun getEntries(list: List<AirQuaData>): List<Entry> {
        val entries: MutableList<Entry> = ArrayList()
        list.forEach {
            if (lastUpdatedTime == 0L) lastUpdatedTime = it.timestamp.getSeconds()
            val interval = it.timestamp.getSeconds() - lastUpdatedTime
            entries.add(Entry(interval.toFloat(), it.aqi.roundToInt().toFloat()))
        }
        return entries
    }

    fun removeObserver() {
        lastUpdatedTime = 0L
        chart_view.clear()
        listLiveData?.removeObservers(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailsAirQuaFragment()
        const val TAG = "DetailsAirQuaFragment"
    }

    override fun onClicked(details: String) {
        (activity as MainActivity).openChart(details)
    }
}