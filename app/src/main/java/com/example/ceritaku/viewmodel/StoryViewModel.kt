package com.example.ceritaku.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ceritaku.data.remote.repository.ApiRepository
import okhttp3.MultipartBody

class StoryViewModel(private val apiRepository: ApiRepository): ViewModel() {

    suspend fun getStoryList(page : Int, size : Int,auth : Any) =
        apiRepository.getStoriesList(page, size, auth)

    suspend fun postStory(file : MultipartBody.Part, desc : String, lat : Float, lon : Float,auth : Any) =
        apiRepository.postStory(file, desc, lat, lon,auth)
}