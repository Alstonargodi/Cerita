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
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }
    private var userDetailModel: UserDetailModel = UserDetailModel()
    private lateinit var userPreferenceConfig: UserPrefrencesConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreferenceConfig = UserPrefrencesConfig(this)


        binding.btnlogin.setOnClickListener {
            sessionChecker()
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

    }


    private fun boxChecker(): Boolean{
        val email = binding.email.text
        val password = binding.password.text
        if(email.isNullOrEmpty())
            return true
        else if (password.isNullOrEmpty())
            return true
        else (password.isNotEmpty() && email.isNotEmpty())
        return false
    }

    private fun sessionChecker(){
        userDetailModel = userPreferenceConfig.getUserDetail()
        val curSession = userDetailModel.name

        if (curSession.isNullOrEmpty()){
            if (boxChecker()){
                showMessage("plese fill the textbox first")
            }else{
                lifecycleScope.launch { login() }
            }
        }else{
            startActivity(
                Intent(this,MainActivity::class.java)
            )
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
                    saveUserLogin(
                        it.data.loginResult.name,
                        it.data.loginResult.token,
                        true,
                        false
                    )
                    showMessage("welcome + ${it.data.loginResult.name}")
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                }
                is Result.Error->{
                    binding.pgbarlogin.visibility = View.GONE
                    if (it.error == invalid){
                        showMessage("Invalid form")
                    }else{
                        showMessage(it.error)
                    }
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

    private fun showMessage(message : String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }


    companion object{
        const val tag = "LoginActivity"
        const val invalid = "HTTP 400 Bad Request"
    }
}