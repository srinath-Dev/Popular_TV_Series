package com.srinath.populartvseries.models

import androidx.room.Entity
import androidx.room.PrimaryKey

data class TvSeries(
    val id: Int,
    val name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val first_air_date: String?,
    val vote_average: Double,
    val vote_count: Int
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
    posterPath = poster_path,
    firstAirDate = first_air_date,
    voteAverage = vote_average,
    voteCount = vote_count
)

fun TvSeriesEntity.toDomain(): TvSeries = TvSeries(
    id = id,
    name = name,
    overview = overview,
    popularity = popularity,
    poster_path = posterPath,
    first_air_date = firstAirDate,
    vote_average = voteAverage,
    vote_count = voteCount
)
