package com.example.ceritaku.data.remote.service

import com.example.ceritaku.data.response.LoginResponse
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.StoryResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email : String,
        @Field("password") password : String,
    ):LoginResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String,
    ): RegisterResponse


    @GET("stories")
    suspend fun getStoriesList(
        @Query("page") page : Int,
        @Query("size") size : Int,
        @Header("Authorization") auth: Any
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Part file: MultipartBody.Part,
        @Part("description") description : String,
        @Part("lat") lat : Float,
        @Part("lon") lon : Float,
        @Header("Authorization") auth: Any
    ): NewStoryResponse

}