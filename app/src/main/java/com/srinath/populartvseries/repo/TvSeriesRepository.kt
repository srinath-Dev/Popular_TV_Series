package com.srinath.populartvseries.repo

import com.srinath.populartvseries.api.TvSeriesApi
import com.srinath.populartvseries.db.TvSeriesDao
import com.srinath.populartvseries.models.TvSeries
import com.srinath.populartvseries.models.toDomain
import com.srinath.populartvseries.models.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TvSeriesRepository(
    private val api: TvSeriesApi,
    private val dao: TvSeriesDao
) {
    fun getPopularSeries(): Flow<List<TvSeries>> {
        return dao.getAllSeries().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun refreshSeries(page: Int) {
        try {
            val response = api.getPopularSeries(page)
            if (response.results.isNotEmpty()) {
                val newEntities = response.results.map { it.toEntity() }
                dao.insertSeries(newEntities) // Assuming this appends new data
            }
        } catch (e: Exception) {
            e.printStackTrace() // Handle the error appropriately
        }
    }
}


