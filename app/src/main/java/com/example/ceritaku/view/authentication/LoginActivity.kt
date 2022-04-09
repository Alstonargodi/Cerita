package com.example.ceritaku.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.data.local.UserPrefrences
import com.example.ceritaku.data.local.entity.UserDetailModel
import com.example.ceritaku.databinding.ActivityLoginBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }
    private lateinit var userDetailModel: UserDetailModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnlogin.setOnClickListener {
            lifecycleScope.launch {
                login()
            }
        }

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
                    Log.d(tag,it.data.message)
                    it.data.apply {
                        saveUserLogin(
                            loginResult.name,
                            loginResult.token,
                            true,
                            false
                        )
                    }
                }
                is Result.Error->{
                    binding.pgbarlogin.visibility = View.GONE
                    Log.d(tag,it.error.toString())
                }
            }
        }
    }

    private fun saveUserLogin(name : String,token : String,onBoard : Boolean,theme : Boolean){
        val userPreferences = UserPrefrences(this)
        userDetailModel.name = name
        userDetailModel.token = token
        userDetailModel.onBoard = onBoard
        userDetailModel.theme = theme

        userPreferences.setUserDetail(userDetailModel)
        Toast.makeText(this,"data tersimpan",Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val tag = "LoginActivity"
    }
}