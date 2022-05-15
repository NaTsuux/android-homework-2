package com.example.drawsim.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse(
    val code: Int,
    @SerialName("msg") val message: String,
    val results: DoggyInfo?
)

@Serializable
data class DoggyInfo(
    @SerialName("name") val doggyName: String,
    val comment: String
)
