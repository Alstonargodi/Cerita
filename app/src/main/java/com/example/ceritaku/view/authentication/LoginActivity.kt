package com.example.ceritaku.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.databinding.ActivityLoginBinding
import com.example.ceritaku.remote.Result
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }

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
                    Log.d("loading",it.data.message)
                    Log.d("loading",it.data.loginResult.name)
                }
                is Result.Error->{
                    binding.pgbarlogin.visibility = View.GONE
                    Log.d("loading",it.error.toString())
                }
            }
        }
    }
}