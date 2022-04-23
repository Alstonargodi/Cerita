package com.example.ceritaku.utils

import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.login.LoginResult
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.service.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService: ApiService {
    private val dummyLoginResponse = DataDummy.genFakeLoginResponseSucces()

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesList(page: Int, size: Int, auth: Any): StoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getMapsStories(page: Int, auth: Any): StoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun postStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float,
        auth: Any
    ): NewStoryResponse {
        TODO("Not yet implemented")
    }


}