package com.example.ceritaku.data.remote.response.story


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class StoryResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("listStory")
    val listStory: List<Story>,
    @SerializedName("message")
    val message: String
)




@Entity(tableName = "Story")
@Parcelize
data class Story(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("lat")
    val lat: Float,
    @SerializedName("lon")
    val lon: Float,
    @SerializedName("name")
    val name: String,
    @SerializedName("photoUrl")
    val photoUrl: String
): Parcelable