package com.example.ceritaku.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.view.utils.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiRepository(private val apiService: ApiService) {


    suspend fun postLogin(email : String,password : String): LiveData<Result<LoginResponse>> =
       liveData {
            emit(Result.Loading)
            try {
                val respon = apiService.postLogin(email, password)
                emit(Result.Sucess(respon))
            }catch (e : Exception){
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun postRegister(name : String,email : String,password: String): LiveData<Result<RegisterResponse>> = liveData{
        emit(Result.Loading)
        try {
            val respon = apiService.postRegister(name, email, password)
            emit(Result.Sucess(respon))
        }catch (e : Exception){
            emit(Result.Error(e.message.toString()))
        }
    }


    fun getStoriesList(auth : String): LiveData<PagingData<Story>>{
        return Pager(
            config = PagingConfig(5),
            pagingSourceFactory = { StoryPagingSource(apiService,auth)}
        ).liveData
    }

    suspend fun postStory(file : MultipartBody.Part, desc : RequestBody, lat : Float, lon : Float, auth : Any)
    :LiveData<Result<NewStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val respon = apiService.postStory(file,desc,lat, lon,auth)
            emit(Result.Sucess(respon))
        }catch (e :  Exception){
            emit(Result.Error(e.message.toString()))
        }
    }


}