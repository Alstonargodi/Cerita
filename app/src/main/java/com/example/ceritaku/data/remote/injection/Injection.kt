package com.example.ceritaku.data.remote.injection

import android.content.Context
import com.example.ceritaku.data.local.datastore.UserPreferences
import com.example.ceritaku.data.local.datastore.dataStore
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.config.ApiConfig
import com.example.ceritaku.data.remote.repository.ApiRepository


object Injection {
    fun provideRepository(context: Context): ApiRepository {
        val database = MediatorDatabase.getDatabase(context)
        return ApiRepository(database,ApiConfig.setApiService(), UserPreferences.getInstance(context.dataStore))
    }

}