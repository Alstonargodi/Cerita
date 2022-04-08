package com.example.ceritaku.remote

import com.example.ceritaku.remote.config.ApiConfig

object Injection {
    fun provideRepository(): ApiRepository{
        return ApiRepository(ApiConfig.setApiService())
    }
}