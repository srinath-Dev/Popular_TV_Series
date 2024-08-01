package com.srinath.populartvseries.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.srinath.populartvseries.DetailActivity
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.viewModel.TvSeriesViewModel

@Composable
fun MainScreen(viewModel: TvSeriesViewModel, navController: NavController) {
    val series by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        SearchBar()

        TvSeriesList(
            series = series,
            listState = listState,
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

@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TvSeriesList(
    series: List<TvSeries>,
    listState: LazyListState,
    isLoading: Boolean,
    loadMore: () -> Unit,
    onItemClick: (TvSeries) -> Unit
) {
    LazyColumn(state = listState) {
        items(series) { tvSeries ->
            TvSeriesItem(tvSeries = tvSeries, onClick = { onItemClick(tvSeries) })
        }

        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Add a placeholder for more items to be loaded
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull() }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500${tvSeries.posterPath}")
                    .crossfade(true)
                    .build()
            ),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(tvSeries.name, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(tvSeries.overview, maxLines = 3, overflow = TextOverflow.Ellipsis)
        }
    }
}
