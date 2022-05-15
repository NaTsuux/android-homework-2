package com.example.drawsim.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "http://172.29.88.16:4507/"
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        Json.asConverterFactory(MediaType.get("application/json"))
    )
    .build()

interface ApiService {
    @GET("photos")
    suspend fun getPhotos(): List<RequestData>

    @GET("{imgUrl}")
    suspend fun getPhoto(@Path("imgUrl") imgUrl: String): ResponseBody
}

object RequestApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}