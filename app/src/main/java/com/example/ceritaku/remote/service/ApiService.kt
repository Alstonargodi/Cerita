package com.example.ceritaku.remote.service

import com.example.ceritaku.remote.response.LoginResponse
import com.example.ceritaku.remote.response.register.RegisterResponse
import com.example.ceritaku.remote.response.story.StoryResponse
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
//    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWp6b2pEWUd4NFV0cUs5clUiLCJpYXQiOjE2NDkzOTc3OTh9.VrMbbMLriptuq8rmNfBGA2VZ88CNVJ6hJm93IAdcg7k")
    suspend fun getStoriesList(
        @Query("page") page : Int,
        @Query("size") size : Int,
        @Header("Authorization") auth: Any
    ): StoryResponse

}