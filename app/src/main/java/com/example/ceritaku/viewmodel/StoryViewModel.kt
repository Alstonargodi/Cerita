package com.example.ceritaku.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ceritaku.remote.ApiRepository

class StoryViewModel(private val apiRepository: ApiRepository): ViewModel() {

    suspend fun getStoryList(page : Int, size : Int,auth : Any) =
        apiRepository.getStoriesList(page, size, auth)
}