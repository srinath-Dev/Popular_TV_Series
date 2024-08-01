package com.srinath.populartvseries.api

import com.srinath.populartvseries.models.TvSeries
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TvSeriesApi {
    @GET("tv/popular")
    suspend fun getPopularSeries(
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): ApiResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val API_KEY = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzNGYwNThjZDIzNjZlNTY5ZjVmYmQ3OTA3MDk0Y2JjMiIsIm5iZiI6MTcyMjUyNTgwMS4zMTQyMDcsInN1YiI6IjVlYWQwZTI5NjBjNTFkMDAyMjkxOWYwYiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.9hQcPmFnCfxRQMtBkICvlXDiJ0esGbTBfldiLuukXv8"
    }
}

data class ApiResponse(
    val page: Int,
    val results: List<TvSeries>,
    val total_results: Int,
    val total_pages: Int
)

object NetworkModule {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", TvSeriesApi.API_KEY)
                .header("accept", "application/json")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(TvSeriesApi.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val tvSeriesApi: TvSeriesApi = retrofit.create(TvSeriesApi::class.java)
}
