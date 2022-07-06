package com.example.ceritaku.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ceritaku.data.remote.injection.Injection
import com.example.ceritaku.data.remote.repository.ApiRepository

@Suppress("UNCHECKED_CAST")
class VModelFactory private constructor(
    private val repository : ApiRepository):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingPrefViewModel::class.java) -> {
                SettingPrefViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object{
        @Volatile
        private var instance : VModelFactory? = null
        fun getInstance(context: Context): VModelFactory =
            instance ?: synchronized(this){
                instance ?: VModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}