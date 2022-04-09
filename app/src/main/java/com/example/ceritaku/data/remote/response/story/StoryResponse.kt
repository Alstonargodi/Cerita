package com.example.ceritaku.data.remote.response.story


import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)

data class Story(
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("lat")
    val lat: Any,
    @SerializedName("lon")
    val lon: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("photoUrl")
    val photoUrl: String
)