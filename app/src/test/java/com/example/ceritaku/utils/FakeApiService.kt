package com.example.ceritaku.utils

import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {


    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return if (email.isEmpty() || password.isEmpty()){
            DataDummy.fakeLoginResponse_Fail()
        }else{
            DataDummy.fakeLoginResponse_Success()
        }

    }

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return if (email.isEmpty() || password.isEmpty() || name.isEmpty()){
            DataDummy.fakeRegisterResponse_Fail()
        }else{
            DataDummy.fakeRegisterResponse_Success()
        }
    }


    override suspend fun getStoriesList(page: Int, size: Int, auth: String): StoryResponse {
        return if (auth.isEmpty()){
            DataDummy.fakeListStoryResponse_Fail()
        }else{
            DataDummy.fakeListStoryResponse_Success()
        }
    }

    override suspend fun getMapsStories(page: Int, auth: String): StoryResponse {
        return if (auth.isEmpty()){
            DataDummy.fakeListStoryResponse_Fail()
        }else{
            DataDummy.fakeListStoryResponse_Success()
        }
    }

    override suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float,
        auth: Any
    ): NewStoryResponse {
        return if(description.contentLength() == 0L || auth.toString().isEmpty()){
            DataDummy.fakePostNewStories_Fail()
        }else{
            DataDummy.fakePostNewStories_Success()
        }
    }


}