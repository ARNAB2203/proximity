package com.assignment.airquality.citylist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.assignment.airquality.MainActivity
import com.assignment.airquality.R
import com.assignment.airquality.citylist.adapter.AirQuaDataAdapter
import com.assignment.airquality.citylist.adapter.AirQuaListener
import com.assignment.airquality.repo.Repo
import com.assignment.airquality.viewmodel.ProximityViewModel
import com.assignment.airquality.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_city_list.*

class CityListFragment : Fragment(), AirQuaListener {
    private var viewModel: ProximityViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = initViewModel()

        viewModel?.getUpdatedData()?.observe(this as LifecycleOwner, { cityList ->
            if (cityList.isNotEmpty()) recycler_view.adapter = AirQuaDataAdapter(cityList, this)
        })
    }

    private fun initViewModel(): ProximityViewModel {
        val model: ProximityViewModel by viewModels { ViewModelFactory(Repo.getInstance(requireContext())) }
        return model
    }

    companion object {
        @JvmStatic
        fun newInstance() = CityListFragment()
        const val TAG = "CityListFragment"
    }

    override fun onClicked(details: String) {
        (activity as MainActivity).openChart(details)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.getUpdatedData()?.removeObservers(this)
    }
}