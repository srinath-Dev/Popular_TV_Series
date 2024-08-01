package com.srinath.populartvseries.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.srinath.populartvseries.R
import com.srinath.populartvseries.activities.DetailActivity
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.viewModel.TvSeriesViewModel

@Composable
fun MainScreen(viewModel: TvSeriesViewModel, navController: NavController) {
    val series by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val gridState = rememberLazyGridState()

    Box(modifier = Modifier.fillMaxSize().background(color = Color.Black)) {
        Column(modifier = Modifier.padding(16.dp)) {
            SearchBar()
            Spacer(modifier = Modifier.height(8.dp))
            TvSeriesGrid(
                series = series,
                gridState = gridState,
                isLoading = isLoading,
                loadMore = {
                    viewModel.loadNextPage()
                }
            ) { selectedSeries ->
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("SERIES_ID", selectedSeries.id)
                }
                context.startActivity(intent)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Search", color = Color.White) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            focusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray
        )
    )
}

@Composable
fun TvSeriesGrid(
    series: List<TvSeries>,
    gridState: LazyGridState,
    isLoading: Boolean,
    loadMore: () -> Unit,
    onItemClick: (TvSeries) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        modifier = Modifier.fillMaxSize()
    ) {
        items(series.size) { index ->
            TvSeriesItem(tvSeries = series[index], onClick = { onItemClick(series[index]) })
        }

        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Red)
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull() }
            .collect { lastVisibleItem ->
                lastVisibleItem?.let {
                    if (lastVisibleItem.index >= series.size - 1 && !isLoading) {
                        loadMore()
                    }
                }
            }
    }
}

@Composable
fun TvSeriesItem(tvSeries: TvSeries, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        val imageUrl = "https://image.tmdb.org/t/p/w500${tvSeries.poster_path}"
        Log.d("TvSeriesItem", "Loading image from URL: $imageUrl")

        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = tvSeries.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "${tvSeries.vote_average}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Popularity: ${tvSeries.popularity}",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
