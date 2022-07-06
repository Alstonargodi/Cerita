package com.example.ceritaku.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.response.story.Story
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val apiRepository: ApiRepository): ViewModel() {

    fun getStoryList(auth : String): LiveData<PagingData<Story>>  =
        apiRepository.getStoriesList(auth).asLiveData().cachedIn(viewModelScope)

    suspend fun getMapsStories(page : Int,auth : String) =
        apiRepository.getLocStories(page, auth)

    suspend fun postStory(file : MultipartBody.Part, desc : RequestBody, lat : Float, lon : Float, auth : Any) =
        apiRepository.postStory(file, desc, lat, lon,auth)

}