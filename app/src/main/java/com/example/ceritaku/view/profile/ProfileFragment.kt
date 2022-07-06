package com.example.ceritaku.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.ceritaku.databinding.FragmentProfileBinding
import com.example.ceritaku.view.authentication.LoginActivity
import com.example.ceritaku.viewmodel.SettingPrefViewModel
import com.example.ceritaku.viewmodel.VModelFactory


@Suppress("SameParameterValue")
class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private val prefViewModel : SettingPrefViewModel by viewModels{ VModelFactory.getInstance(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)


        prefViewModel.getUserName().observe(viewLifecycleOwner){ name ->
            binding.curprofile.text = name
        }


        binding.btnlogout.setOnClickListener {
            logout()
        }

        binding.btnlanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return binding.root
    }

    private fun logout(){
        prefViewModel.saveThemeSetting(
            true,
            "",
            "",
        )
        startActivity(Intent(context,LoginActivity::class.java))
        activity?.finishAffinity()
    }





}