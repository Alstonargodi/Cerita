package com.example.ceritaku.data.remote.injection

import com.example.ceritaku.data.remote.config.ApiConfig
import com.example.ceritaku.data.remote.repository.ApiRepository

object Injection {
    fun provideRepository(): ApiRepository {
        return ApiRepository(ApiConfig.setApiService())
    }
}