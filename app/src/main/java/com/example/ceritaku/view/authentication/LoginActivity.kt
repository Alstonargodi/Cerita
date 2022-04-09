package com.example.ceritaku.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.MainActivity
import com.example.ceritaku.data.local.UserPrefrencesConfig
import com.example.ceritaku.data.local.entity.UserDetailModel
import com.example.ceritaku.databinding.ActivityLoginBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }
    private var userDetailModel: UserDetailModel = UserDetailModel()
    private lateinit var userPrefrencesConfig: UserPrefrencesConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPrefrencesConfig = UserPrefrencesConfig(this)

        binding.btnlogin.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }
        userChecker()


        binding.register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }

    private suspend fun login(){
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.postLogin(email,password).observe(this){
            when(it){
                is Result.Loading->{
                    binding.pgbarlogin.visibility = View.VISIBLE
                }
                is Result.Sucess->{
                    binding.pgbarlogin.visibility = View.GONE
                    Log.d(tag, it.data.loginResult.name)
                    saveUserLogin(
                        it.data.loginResult.name,
                        it.data.loginResult.token,
                        true,
                        false
                    )
                    Log.d(tag, it.data.loginResult.name)

                }
                is Result.Error->{
                    binding.pgbarlogin.visibility = View.GONE
                    Log.d(tag, it.error)
                }
            }
        }
    }

    private fun saveUserLogin(name : String, token : String, onBoard : Boolean, theme : Boolean){
        try {
            val userPreferences = UserPrefrencesConfig(this)
            userDetailModel.name = name
            userDetailModel.token = token
            userDetailModel.onBoard = onBoard
            userDetailModel.theme = theme
            userPreferences.setUserDetail(userDetailModel)
        }catch (e : Exception){
            Log.d(tag, "fail ${e.message}")
        }

    }

    private fun userChecker(){
        userDetailModel = userPrefrencesConfig.getUserDetail()
        if (userDetailModel.name != null){
            showSnackbar(userDetailModel.name.toString())
            lifecycleScope.launch {
                delay(2000L)
                startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            }

        }
    }

    private fun showSnackbar(message : String){
        Snackbar.make(
            binding.root,
            "Welcome $message",
            Snackbar.LENGTH_LONG
        ).show()
    }

    companion object{
        const val tag = "LoginActivity"
    }
}