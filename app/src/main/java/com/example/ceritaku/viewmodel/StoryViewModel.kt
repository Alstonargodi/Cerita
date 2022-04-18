package com.example.ceritaku.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.view.utils.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(private val apiRepository: ApiRepository): ViewModel() {

    fun getStoryList(auth : String): LiveData<PagingData<Story>>  =
        apiRepository.getStoriesList(auth).cachedIn(viewModelScope)


    suspend fun getMapsStories(page : Int,auth : String) =
        apiRepository.getLocStories(page, auth)

    suspend fun postStory(file : MultipartBody.Part, desc : RequestBody, lat : Float, lon : Float, auth : Any) =
        apiRepository.postStory(file, desc, lat, lon,auth)


    private val _isEmpty = MutableLiveData(true)
    val isEmpty : LiveData<Boolean> = _isEmpty
    fun setEmptys(status : Boolean){
        _isEmpty.value = status
    }
}