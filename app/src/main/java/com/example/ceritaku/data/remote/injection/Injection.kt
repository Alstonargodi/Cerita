package com.example.ceritaku.data.remote.injection

import android.content.Context
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.config.ApiConfig
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.view.utils.paging.StoryRemoteMediator

object Injection {
    fun provideRepository(context: Context): ApiRepository {
        val database = MediatorDatabase.getDatabase(context)
        return ApiRepository(database,ApiConfig.setApiService())
    }

}