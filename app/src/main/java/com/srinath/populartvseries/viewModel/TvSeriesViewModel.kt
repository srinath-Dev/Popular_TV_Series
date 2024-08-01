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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 1

    init {
        loadNextPage() // Load the first page initially
    }

    fun loadNextPage() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.refreshSeries(currentPage) // Refresh the series data for the current page

                // Collect the series from the repository and update UI state
                repository.getPopularSeries().collect { newSeries ->
                    _uiState.value = _uiState.value + newSeries
                    currentPage++
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handle the error appropriately
                _isLoading.value = false
            }
        }
    }
}
