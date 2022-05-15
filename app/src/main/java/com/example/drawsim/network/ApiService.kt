package com.example.drawsim.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "http://172.29.88.16:8080/api/"
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        Json.asConverterFactory(MediaType.get("application/json"))
    )
    .build()

interface ApiService {
    @GET("rand-doggy")
    suspend fun randDoggy(): BaseResponse

    @GET("get-doggy")
    suspend fun getDoggy(@Query("name") doggyName: String): ResponseBody
}

object RequestApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}