package com.srinath.populartvseries

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.srinath.populartvseries.api.NetworkModule
import com.srinath.populartvseries.db.DatabaseModule
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.repo.TvSeriesRepository
import com.srinath.populartvseries.screens.MainScreen
import com.srinath.populartvseries.ui.theme.PopularTVSeriesTheme
import com.srinath.populartvseries.viewModel.TvSeriesViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: TvSeriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = DatabaseModule.getDatabase(this)
        val repository = TvSeriesRepository(NetworkModule.tvSeriesApi, database.tvSeriesDao())
        val factory = TvSeriesViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(TvSeriesViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                TvSeriesApp(viewModel = viewModel)
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TvSeriesApp(viewModel: TvSeriesViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            "detail/{seriesId}",
            arguments = listOf(navArgument("seriesId") { type = NavType.IntType })
        ) { backStackEntry ->
            val seriesId = backStackEntry.arguments?.getInt("seriesId")
            val series = viewModel.uiState.value.find { it.id == seriesId }
            series?.let { DetailScreen(tvSeries = it) }
        }
    }
}


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

@Composable
fun DetailScreen(tvSeries: TvSeries) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberImagePainter(
                data = "https://image.tmdb.org/t/p/w500${tvSeries.posterPath}"
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(tvSeries.name, style = MaterialTheme.typography.displaySmall)
        Text(
            "Rating: ${tvSeries.voteAverage} (${tvSeries.voteCount} votes)",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(tvSeries.overview, style = MaterialTheme.typography.bodyLarge)
    }


}


class TvSeriesViewModelFactory(
    private val repository: TvSeriesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TvSeriesViewModel::class.java)) {
            return TvSeriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

