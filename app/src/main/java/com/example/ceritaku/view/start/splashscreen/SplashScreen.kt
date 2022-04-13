package com.example.ceritaku.view.start.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.local.UserPrefrencesConfig
import com.example.ceritaku.data.local.entity.UserDetailModel
import com.example.ceritaku.view.authentication.LoginActivity
import com.example.ceritaku.view.start.onboarding.OnBoardingActivity

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private var userDetailModel: UserDetailModel = UserDetailModel()
    private lateinit var userPreferenceConfig: UserPrefrencesConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userPreferenceConfig = UserPrefrencesConfig(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        Handler(Looper.getMainLooper()).postDelayed({
            sessionChecker()
        }, 2000)

    }

    private fun sessionChecker(){
        userDetailModel = userPreferenceConfig.getUserDetail()
        val curSessionBoard = userDetailModel.onBoard
        val curSessionName = userDetailModel.name

        if (curSessionBoard){
            if (curSessionName != null) {
                if (curSessionName.isEmpty()){
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }
        }else{
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finishAffinity()
        }
    }
}