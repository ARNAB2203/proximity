package com.assignment.airquality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.assignment.airquality.citylist.view.CityListFragment
import com.assignment.airquality.graph.DetailsAirQuaFragment
import com.assignment.airquality.repo.Repo
import com.assignment.airquality.viewmodel.ProximityViewModel
import com.assignment.airquality.viewmodel.ViewModelFactory
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var viewModel: ProximityViewModel

    private val cityListFragment = CityListFragment.newInstance()
    private val detailsAirQuaFragment = DetailsAirQuaFragment.newInstance()
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = initViewModel()

        loadFragments()
    }

    private fun loadFragments() {
        supportFragmentManager.findFragmentByTag(DetailsAirQuaFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, detailsAirQuaFragment, DetailsAirQuaFragment.TAG)
            .hide(detailsAirQuaFragment)
            .commitAllowingStateLoss()

        supportFragmentManager.findFragmentByTag(CityListFragment.TAG) ?: supportFragmentManager
            .beginTransaction()
            .add(R.id.container_fragment, cityListFragment, CityListFragment.TAG)
            .commitAllowingStateLoss()

        activeFragment = supportFragmentManager.findFragmentByTag(CityListFragment.TAG) ?: cityListFragment
    }

    private fun initViewModel(): ProximityViewModel {
        val model: ProximityViewModel by viewModels { ViewModelFactory(Repo.getInstance(this)) }
        return model
    }
    override fun onResume() {
        super.onResume()
        initWebSocket()
        webSocketClient.connect()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }


    private fun initWebSocket() {
        val airQuaUri = URI(WEB_SOCKET_URL)
        createWebSocketClient(airQuaUri)
    }

    private fun createWebSocketClient(airQuaUri: URI?) {
        webSocketClient = object : WebSocketClient(airQuaUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen() Called")
            }
            override fun onMessage(message: String?) {
                Log.i(TAG, "onMessage: $message")
                saveAqiData(message)
            }
            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose: code -> $code, reason -> $reason, remote -> $remote")
            }
            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }
        }
    }

    private fun saveAqiData(message: String?) {
        message?.let {
            val list = viewModel.parseMessage(it)
            viewModel.insertData(list)
        }
    }

    private fun showFragment(fragment: Fragment) {
        activeFragment?.run {
            supportFragmentManager.beginTransaction().hide(this).show(fragment).commit()
            activeFragment = fragment
        }
    }

    fun openChart(details: String) {
        showFragment(detailsAirQuaFragment)
        detailsAirQuaFragment.updateCity(details)
    }
    override fun onBackPressed() {
        if (detailsAirQuaFragment.isVisible) closeChart()
        else super.onBackPressed()
    }

    private fun closeChart() {
        showFragment(cityListFragment)
        detailsAirQuaFragment.removeObserver()
    }

    companion object {
        const val WEB_SOCKET_URL = "ws://city-ws.herokuapp.com/"
        const val TAG = "Proximity"
    }
}