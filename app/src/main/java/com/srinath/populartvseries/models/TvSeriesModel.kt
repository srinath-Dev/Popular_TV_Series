package com.srinath.populartvseries.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val firstAirDate: String?,
    val voteAverage: Double,
    val voteCount: Int
)

@Entity(tableName = "series")
data class TvSeriesEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val firstAirDate: String?,
    val voteAverage: Double,
    val voteCount: Int
)

fun TvSeries.toEntity(): TvSeriesEntity = TvSeriesEntity(
    id = id,
    name = name,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    firstAirDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)

fun TvSeriesEntity.toDomain(): TvSeries = TvSeries(
    id = id,
    name = name,
    overview = overview,
    popularity = popularity,
    posterPath = posterPath,
    firstAirDate = firstAirDate,
    voteAverage = voteAverage,
    voteCount = voteCount
)
