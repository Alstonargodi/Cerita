package com.example.ceritaku.view.start.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.local.preferences.UserPrefrencesConfig
import com.example.ceritaku.data.local.preferences.entity.UserDetailModel
import com.example.ceritaku.data.local.store.UserPrefrences
import com.example.ceritaku.data.local.store.dataStore
import com.example.ceritaku.view.authentication.LoginActivity
import com.example.ceritaku.view.start.onboarding.OnBoardingActivity
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var prefViewModel : SettingPrefViewModel
    private lateinit var userPreferenceConfig: UserPrefrencesConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        userPreferenceConfig = UserPrefrencesConfig(this)

        prefViewModel = ViewModelProvider(this,
            PrefViewModelFactory(UserPrefrences.getInstance(dataStore))
        )[SettingPrefViewModel::class.java]

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        Handler(Looper.getMainLooper()).postDelayed({
            sessionChecker()
        }, 2000)

    }

    private fun sessionChecker(){
        prefViewModel.apply {
            getOnBoardStatus().observe(this@SplashScreen){ curSessionBoard ->
                getUserName().observe(this@SplashScreen){  curSessionName ->
                    if (curSessionBoard){
                        if (curSessionName != null) {
                            if (curSessionName.isEmpty()){
                                startActivity(Intent(this@SplashScreen, LoginActivity::class.java))
                                finishAffinity()
                            }else{
                                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                                finishAffinity()
                            }
                        }
                    }else{
                        startActivity(Intent(this@SplashScreen, OnBoardingActivity::class.java))
                        finishAffinity()
                    }
                }
            }
        }
    }
}