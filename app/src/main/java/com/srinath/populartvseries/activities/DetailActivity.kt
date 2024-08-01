package com.srinath.populartvseries.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImage
import com.srinath.populartvseries.TvSeriesViewModelFactory
import com.srinath.populartvseries.api.NetworkModule
import com.srinath.populartvseries.db.DatabaseModule
import com.srinath.populartvseries.models.TvSeries
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
            val tvSeries = intent.getSerializableExtra("TV_SERIES") as? TvSeries
            if (tvSeries != null) {
                setContent {
                    MaterialTheme {
                        DetailScreen(tvSeries = tvSeries)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailScreen(tvSeries: TvSeries) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        item {
            // Poster Image
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${tvSeries.poster_path}",
                contentDescription = tvSeries.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                text = tvSeries.name,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Overview Section
            OverviewSection(overview = tvSeries.overview)

            Spacer(modifier = Modifier.height(16.dp))

            // Additional Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetailItem(title = "Release Date", value = tvSeries.first_air_date ?: "N/A")
                DetailItem(title = "Rating", value = "${tvSeries.vote_average}/10")
                DetailItem(title = "Popularity", value = tvSeries.popularity.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Sections for Cast, Episodes, Reviews
        items(listOf("Cast", "Episodes", "Reviews")) { section ->
            ListItem(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { /* Handle navigation to section */ },
                headlineContent = {
                    Text(text = section, color = Color.White)
                }
            )
        }
    }
}

@Composable
fun OverviewSection(overview: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(Color.DarkGray)
            .padding(16.dp)
    ) {
        Text(
            text = "Overview",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = overview,
            color = Color.Gray,
            fontSize = 16.sp,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
