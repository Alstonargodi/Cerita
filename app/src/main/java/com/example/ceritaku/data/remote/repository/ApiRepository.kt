package com.example.ceritaku.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.ceritaku.data.local.datastore.UserPreferences
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.view.utils.paging.StoryRemoteMediator
import com.example.ceritaku.view.utils.wrapperIdling
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiRepository(
    private val mediatorDatabase: MediatorDatabase,
    private val apiService: ApiService,
    private val preferences: UserPreferences
    ) {

    suspend fun postLogin(email : String,password : String): LiveData<MediatorResult<LoginResponse>> = wrapperIdling {
        liveData {
            emit(MediatorResult.Loading)
            try {
                val respon = apiService.postLogin(email, password)
                emit(MediatorResult.Sucess(respon))
            }catch (e : Exception){
                emit(MediatorResult.Error(e.message.toString()))
            }
        }
    }


    suspend fun postRegister(name : String,email : String,password: String): LiveData<MediatorResult<RegisterResponse>> = wrapperIdling {
        liveData{
            emit(MediatorResult.Loading)
            try {
                val respon = apiService.postRegister(name, email, password)
                emit(MediatorResult.Sucess(respon))
            }catch (e : Exception){
                emit(MediatorResult.Error(e.message.toString()))
            }
        }
    }

    suspend fun getLocStories(page : Int,auth : String): LiveData<MediatorResult<StoryResponse>> = wrapperIdling {
        liveData {
            emit(MediatorResult.Loading)
            try {
                val respon = apiService.getMapsStories(page,auth)
                emit(MediatorResult.Sucess(respon))
            }catch (e : Exception){
                emit(MediatorResult.Error(e.message.toString()))
            }
        }
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getStoriesList(auth : String): Flow<PagingData<Story>> = wrapperIdling {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = true),
            remoteMediator = StoryRemoteMediator(mediatorDatabase,apiService,auth),
            pagingSourceFactory = { mediatorDatabase.storyDao().getAllStory()}
        ).flow
    }



    suspend fun postStory(file : MultipartBody.Part, desc : RequestBody, lat : Float, lon : Float, auth : Any)
    :LiveData<MediatorResult<NewStoryResponse>> = wrapperIdling {
        liveData {
            emit(MediatorResult.Loading)
            try {
                val respon = apiService.postStory(file,desc,lat, lon,auth)
                emit(MediatorResult.Sucess(respon))
            }catch (e :  Exception){
                emit(MediatorResult.Error(e.message.toString()))
            }
        }
    }

    fun getUserToken(): LiveData<String> = preferences.getUserToken().asLiveData()
    fun getUserName(): LiveData<String> = preferences.getUserName().asLiveData()
    fun getOnBoardStatus(): LiveData<Boolean> = preferences.getonBoardStatus().asLiveData()

    suspend fun savePrefrences(onBoardStatus : Boolean, name : String, token : String){
        preferences.savePrefrences(onBoardStatus,name, token)
    }

}