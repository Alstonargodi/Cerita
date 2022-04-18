package com.example.ceritaku.viewmodel.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ceritaku.data.local.datastore.UserPrefrences
import kotlinx.coroutines.launch

class SettingPrefViewModel(private val prefrences : UserPrefrences): ViewModel() {

    fun getUserToken(): LiveData<String> = prefrences.getUserToken().asLiveData()
    fun getUserName(): LiveData<String> = prefrences.getUserName().asLiveData()
    fun getOnBoardStatus(): LiveData<Boolean> = prefrences.getonBoardStatus().asLiveData()

    fun saveThemeSetting(onBoardStatus : Boolean,name : String,token : String){
        viewModelScope.launch {
            prefrences.savePrefrences(onBoardStatus,name, token)
        }
    }
}