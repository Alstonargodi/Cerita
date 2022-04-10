package com.example.ceritaku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.ceritaku.databinding.ActivityMainBinding
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.upload.CameraFragment
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            }

            true
        }

    }


    private fun setFragment(fragment : Fragment){
        val supFragment = supportFragmentManager
        val transFragment = supFragment.beginTransaction()
        transFragment
            .replace(R.id.fragmentview,fragment)
            .commit()

    }




}