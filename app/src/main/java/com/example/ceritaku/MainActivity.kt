package com.example.ceritaku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.ceritaku.databinding.ActivityMainBinding
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.making.NewStoryFragment
import com.example.ceritaku.viewmodel.StoryViewModel
import com.example.ceritaku.viewmodel.VModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : StoryViewModel by viewModels{
        VModelFactory.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        binding.bottommenu.setOnItemSelectedListener{
            when(it.itemId){
                R.id.rumah-> {
                    setFragment(HomeFragment())
                }
                R.id.add-> {
                    setFragment(NewStoryFragment())
                    binding.bottommenu.visibility = View.GONE
                }
            }

            true
        }

    }


    private fun setFragment(fragment : Fragment){
        val supFragment = supportFragmentManager
        val transFragment = supFragment.beginTransaction()
        transFragment.replace(R.id.fragmentview,fragment)
        transFragment.commit()

    }

}