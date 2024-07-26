package com.example.todo.fragments

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TimeApiService {
    @GET("current/zone")
    suspend fun getCurrentTime(@Query("timeZone") timeZone: String): TimeApiResponse
}

object RetrofitInstance {
    private const val BASE_URL = "https://timeapi.io/api/Time/"

    val api: TimeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimeApiService::class.java)
    }
}
