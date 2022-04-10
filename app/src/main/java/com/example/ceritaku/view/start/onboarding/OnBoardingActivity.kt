package com.example.ceritaku.view.start.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ceritaku.R
import com.example.ceritaku.databinding.ActivityOnboardingBinding
import com.example.ceritaku.view.authentication.LoginActivity

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvlogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}