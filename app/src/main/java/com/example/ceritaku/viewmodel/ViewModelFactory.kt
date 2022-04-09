package com.example.ceritaku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.injection.Injection

class VModelFactory private constructor(private val repository : ApiRepository):
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)){
            return AuthViewModel(repository) as T
        }else if (modelClass.isAssignableFrom(StoryViewModel::class.java)){
            return StoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object{
        @Volatile
        private var instance : VModelFactory? = null
        fun getInstance(): VModelFactory =
            instance ?: synchronized(this){
                instance ?: VModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}