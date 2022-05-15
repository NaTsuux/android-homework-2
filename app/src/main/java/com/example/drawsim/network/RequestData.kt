package com.example.drawsim.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestData (
    val id: Int,
    @SerialName("img_src") val imgSrc: String,
)

