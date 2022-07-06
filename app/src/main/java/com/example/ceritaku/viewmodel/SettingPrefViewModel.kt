package com.example.ceritaku.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceritaku.data.remote.repository.ApiRepository
import kotlinx.coroutines.launch

class SettingPrefViewModel(private val repository: ApiRepository): ViewModel() {

    fun getUserToken(): LiveData<String> = repository.getUserToken()

    fun getUserName(): LiveData<String> = repository.getUserName()
    fun getOnBoardStatus(): LiveData<Boolean> = repository.getOnBoardStatus()

    fun saveThemeSetting(onBoardStatus : Boolean,name : String,token : String){
        viewModelScope.launch {
            repository.savePrefrences(onBoardStatus,name, token)
        }
    }
}