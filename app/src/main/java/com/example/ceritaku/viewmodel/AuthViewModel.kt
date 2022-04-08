package com.example.ceritaku.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ceritaku.remote.ApiRepository
import com.example.ceritaku.remote.config.ApiConfig
import com.example.ceritaku.remote.response.LoginResponse
import com.example.ceritaku.remote.response.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val apiRepository: ApiRepository): ViewModel() {

    suspend fun postLogin(name : String, password : String) =
        apiRepository.postLogin(name,password)

    suspend fun postRegister(name : String,email : String,password: String) =
        apiRepository.postRegister(name, email, password)

}