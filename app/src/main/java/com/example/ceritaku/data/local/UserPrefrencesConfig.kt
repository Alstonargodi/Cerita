package com.example.ceritaku.data.local

import android.content.Context
import com.example.ceritaku.data.local.entity.UserDetailModel

internal class UserPrefrencesConfig(context: Context) {
    companion object{
        private const val name = "pref_name"
        private const val token = "pref_token"
        private const val onBoard = "pref_oBoard"
        private const val theme = "pref_theme"
    }

    private val preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun setUserDetail(data : UserDetailModel){
        val editor = preferences.edit()
        editor.putString(name,data.name)
        editor.putString(token,data.token)
        editor.putBoolean(onBoard,data.onBoard)
        editor.putBoolean(theme,data.theme)
        editor.apply()
    }

    fun getUserDetail(): UserDetailModel{
        val model = UserDetailModel()
        model.name = preferences.getString(name, "")
        model.token = preferences.getString(token, "")
        model.onBoard = preferences.getBoolean(onBoard, false)
        model.theme = preferences.getBoolean(theme, false)

        return model
    }

}