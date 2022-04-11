package com.example.ceritaku.view.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.data.local.UserPrefrencesConfig
import com.example.ceritaku.data.local.entity.UserDetailModel
import com.example.ceritaku.databinding.FragmentProfileBinding
import com.example.ceritaku.view.authentication.LoginActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Suppress("SameParameterValue")
class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    private var userDetailModel: UserDetailModel = UserDetailModel()
    private lateinit var userPreferenceConfig: UserPrefrencesConfig

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        userPreferenceConfig = UserPrefrencesConfig(requireContext())
        userDetailModel = userPreferenceConfig.getUserDetail()
        val curSessionName = userDetailModel.name

        binding.curprofile.text = curSessionName


        binding.btnlogout.setOnClickListener {
            showMessage("logging out")
            lifecycleScope.launch {
                delay(1000)
                logout()
            }
        }

        binding.btnlanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        return binding.root
    }

    private fun logout(){
        try {
            val userPreferences = UserPrefrencesConfig(requireContext())
            userDetailModel.name = ""
            userDetailModel.token = ""
            userDetailModel.onBoard = true
            userDetailModel.theme = false
            userPreferences.setUserDetail(userDetailModel)
            startActivity(Intent(context,LoginActivity::class.java))
        }catch (e : Exception){
            Log.d(LoginActivity.tag, "fail ${e.message}")
        }
    }

    private fun showMessage(message : String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}