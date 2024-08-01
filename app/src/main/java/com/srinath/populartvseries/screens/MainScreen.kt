package com.srinath.populartvseries.screens

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.srinath.populartvseries.R
import com.srinath.populartvseries.activities.DetailActivity
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.viewModel.LoadState
import com.srinath.populartvseries.viewModel.TvSeriesViewModel

@Composable
fun MainScreen(viewModel: TvSeriesViewModel, navController: NavController) {
    val series by viewModel.uiState.collectAsState()
    val loadState by viewModel.loadState.collectAsState()
    val context = LocalContext.current
    val gridState = rememberLazyGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(top = 20.dp)
    ) {
        AppBar()

        Spacer(modifier = Modifier.height(8.dp))
        SearchAndSortBar()
        Spacer(modifier = Modifier.height(8.dp))
        TvSeriesGrid(
            series = series,
            gridState = gridState,
            loadState = loadState,
            loadMore = {
                viewModel.loadNextPage()
            }
        ) { selectedSeries ->
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("SERIES_ID", selectedSeries.id)
            }
            context.startActivity(intent)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://www.srinathdev.me/img/night.png")
                    .crossfade(true)
                    .build()
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .height(40.dp)
                .weight(1f)
                .fillMaxHeight(), // Ensure the Box takes up the remaining height
            contentAlignment = Alignment.Center // Center-align content within the Box
        ) {
            Text(
                text = "TMB Popular Series",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndSortBar() {
    var searchQuery by remember { mutableStateOf("") }

    // Define a common height for both search bar and sort icon
    val commonHeight = 56.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 0.dp, start = 8.dp, end = 8.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search.. (Made With ❤\uFE0FSrinath)", color = Color.White, fontSize = 12.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, // Use the built-in search icon
                    contentDescription = "Search.. (Made With ❤️Srinath)",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp) // Adjust size as needed
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(commonHeight), // Set the height for the search box
            shape = RoundedCornerShape(12.dp), // Add rounded corners
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.Gray
            )
        )
        IconButton(
            onClick = { /* Handle sort click */ },
            modifier = Modifier.height(commonHeight) // Set the same height for the sort icon
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_sort_24), // Use the drawable resource
                contentDescription = "Sort",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically), // Adjust size as needed
                colorFilter = ColorFilter.tint(Color.White) // Apply tint color
            )
        }
    }
}

@Composable
fun AnimatedLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val size by infiniteTransition.animateValue(
        initialValue = 24.dp,
        targetValue = 36.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.Red,
                modifier = Modifier.size(size) // Dynamic size based on animation
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Loading more series for you...",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun TvSeriesGrid(
    series: List<TvSeries>,
    gridState: LazyGridState,
    loadState: LoadState,
    loadMore: () -> Unit,
    onItemClick: (TvSeries) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            state = gridState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(series.size) { index ->
                TvSeriesItem(tvSeries = series[index], onClick = { onItemClick(series[index]) })

                // Trigger loadMore when the last item is visible
                if (index == series.lastIndex && loadState != LoadState.Loading) {
                    LaunchedEffect(Unit) {
                        loadMore()
                    }
                }
            }

            item {
                when (loadState) {
                    is LoadState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            if(series.isNotEmpty()){
                               AnimatedLoadingIndicator()
                            }
                        }
                    }
                    is LoadState.Error -> {
                        Text(
                            text = "Error: ${loadState.exception.localizedMessage}",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    else -> {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        if (loadState is LoadState.Loading) {
           if(series.isEmpty()){
               Box(
                   modifier = Modifier
                       .align(Alignment.Center)
                       .padding(16.dp),
                   contentAlignment = Alignment.Center
               ) {
                   AnimatedLoadingIndicator()
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

        // Use the LoadableImage composable
        LoadableImage(imageUrl = imageUrl)

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

@Composable
fun LoadableImage(imageUrl: String) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build()
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(Color.Gray) // Background color for the image area
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop // Adjust content scale as needed
        )
        if (painter.state is AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}
