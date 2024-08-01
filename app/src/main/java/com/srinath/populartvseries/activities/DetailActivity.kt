package com.srinath.populartvseries.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider
import com.srinath.populartvseries.DetailScreen
import com.srinath.populartvseries.TvSeriesViewModelFactory
import com.srinath.populartvseries.api.NetworkModule
import com.srinath.populartvseries.db.DatabaseModule
import com.srinath.populartvseries.repo.TvSeriesRepository
import com.srinath.populartvseries.viewModel.TvSeriesViewModel

class DetailActivity : ComponentActivity() {
    private lateinit var viewModel: TvSeriesViewModel

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val seriesId = intent.getIntExtra("SERIES_ID", 0)
        val database = DatabaseModule.getDatabase(this)
        val repository = TvSeriesRepository(NetworkModule.tvSeriesApi, database.tvSeriesDao())
        val factory = TvSeriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(TvSeriesViewModel::class.java)

        setContent {
            val series = viewModel.uiState.value.find { it.id == seriesId }
            series?.let {
                MaterialTheme {
                    DetailScreen(tvSeries = it)
                }
            }
        }
    }
}