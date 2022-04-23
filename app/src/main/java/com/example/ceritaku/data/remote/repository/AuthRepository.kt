package com.example.ceritaku.data.remote.repository

import com.example.ceritaku.data.remote.response.login.LoginResponse

interface AuthRepository {
    suspend fun getLoginResponse(email : String,password : String): LoginResponse
}