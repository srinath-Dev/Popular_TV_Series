package com.srinath.populartvseries.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.repo.TvSeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TvSeriesViewModel(private val repository: TvSeriesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<List<TvSeries>>(emptyList())
    val uiState: StateFlow<List<TvSeries>> = _uiState.asStateFlow()

    private val _loadState = MutableStateFlow<LoadState>(LoadState.Idle)
    val loadState: StateFlow<LoadState> = _loadState.asStateFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        loadNextPage() // Load the first page initially
    }

    fun loadNextPage() {
        if (_loadState.value is LoadState.Loading || isLastPage) return

        _loadState.value = LoadState.Loading
        viewModelScope.launch {
            try {
                repository.refreshSeries(currentPage) // Refresh the series data for the current page
                repository.getPopularSeries().collect { newSeries ->
                    if (newSeries.isEmpty()) {
                        isLastPage = true
                    } else {
                        _uiState.value = _uiState.value + newSeries
                        currentPage++
                    }
                    _loadState.value = LoadState.Idle
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handle the error appropriately
                _loadState.value = LoadState.Error(e)
            }
        }
    }
}

sealed class LoadState {
    object Idle : LoadState()
    object Loading : LoadState()
    data class Error(val exception: Throwable) : LoadState()
}

