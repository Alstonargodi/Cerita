package com.example.ceritaku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.injection.Injection
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.databinding.ActivityMainBinding
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.profile.ProfileFragment
import com.example.ceritaku.view.upload.CameraFragment
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        binding.bottommenu.visibility = View.VISIBLE
        binding.bottommenu.setOnItemSelectedListener{
            when(it.itemId){
                R.id.rumah-> {
                    setFragment(HomeFragment())
                }
                R.id.add-> {
                    setFragment(CameraFragment())
                    binding.bottommenu.visibility = View.GONE
                }
                R.id.setting->{
                    setFragment(ProfileFragment())
                }
            }

            true
        }


    }

    private fun setFragment(fragment : Fragment){
        val supFragment = supportFragmentManager
        val transFragment = supFragment.beginTransaction()
        transFragment
            .replace(R.id.fragmentviemain,fragment)
            .commit()
    }


}