package com.example.ceritaku.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences (
    private val dataStore : DataStore<Preferences>
) {
    private val onBoardKey = booleanPreferencesKey("onBoard")
    private val tokenKey = stringPreferencesKey("UserToken")
    private val nameKey = stringPreferencesKey("NameKey")

    //READ
    fun getonBoardStatus(): Flow<Boolean> {
        return dataStore.data.map { pref ->
            pref[onBoardKey] ?: false
        }
    }
    fun getUserToken(): Flow<String> {
        return dataStore.data.map { prefrences ->
            prefrences[tokenKey] ?: ""
        }
    }
    fun getUserName(): Flow<String> {
        return dataStore.data.map { prefrences ->
            prefrences[nameKey] ?: ""
        }
    }

    suspend fun savePrefrences(onBoard : Boolean,name : String,token : String){
        dataStore.edit { preferences ->
            preferences[nameKey] = name
            preferences[onBoardKey] = onBoard
            preferences[tokenKey] = token
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }


}