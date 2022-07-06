package com.example.ceritaku.data.remote.response.story


import com.google.gson.annotations.SerializedName

data class NewStoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)