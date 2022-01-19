package com.assignment.airquality

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.assignment.airquality.repo.Repo
import com.assignment.airquality.viewmodel.ProximityViewModel
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProximityViewModelTest : TestCase() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProximityViewModel

    private lateinit var repo: Repo

    private lateinit var context: Context

    @Before
    public override fun setUp() {
        super.setUp()
        context = ApplicationProvider.getApplicationContext()
        repo = Repo.getInstance(context)
        viewModel = ProximityViewModel(repo)
    }

    @Test
    fun testInsertData() {
        val data = "[{\"city\":\"Delhi\",\"aqi\":298.64080776518347},{\"city\":\"Kolkata\"," +
                "\"aqi\":198.11577918868647},{\"city\":\"Bhubaneswar\",\"aqi\":102.40529201642094}," +
                "{\"city\":\"Pune\",\"aqi\":218.03701501684213},{\"city\":\"Hyderabad\",\"aqi\":203.90122336612518}," +
                "{\"city\":\"Indore\",\"aqi\":52.53902022903398},{\"city\":\"Jaipur\",\"aqi\":141.79251165976967}]"
        val list = viewModel.parseMessage(data)
        viewModel.insertData(list)
    }
}