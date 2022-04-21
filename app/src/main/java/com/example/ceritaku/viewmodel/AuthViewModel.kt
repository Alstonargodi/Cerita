package com.example.ceritaku.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ceritaku.data.remote.repository.ApiRepository

class AuthViewModel(private val apiRepository: ApiRepository): ViewModel() {

    suspend fun postLogin(name : String, password : String) =
        apiRepository.postLogin(name,password)

    suspend fun postRegister(name : String,email : String,password: String) =
        apiRepository.postRegister(name, email, password)

}
