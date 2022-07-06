package com.example.ceritaku.view.start.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.view.authentication.LoginActivity
import com.example.ceritaku.view.start.onboarding.OnBoardingActivity
import com.example.ceritaku.viewmodel.SettingPrefViewModel
import com.example.ceritaku.viewmodel.VModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val prefViewModel : SettingPrefViewModel by viewModels{ VModelFactory.getInstance(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        Handler(Looper.getMainLooper()).postDelayed({
            sessionChecker()
        }, 2000)

    }

    private fun sessionChecker(){
        prefViewModel.apply {
            getOnBoardStatus().observe(this@SplashScreenActivity){ curSessionBoard ->
                getUserName().observe(this@SplashScreenActivity){ curSessionName ->
                    if (curSessionBoard){
                        if (curSessionName != null) {
                            if (curSessionName.isEmpty()){
                                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                                finishAffinity()
                            }else{
                                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                                finishAffinity()
                            }
                        }
                    }else{
                        startActivity(Intent(this@SplashScreenActivity, OnBoardingActivity::class.java))
                        finishAffinity()
                    }
                }
            }
        }
    }
}