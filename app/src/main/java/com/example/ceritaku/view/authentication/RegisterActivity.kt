package com.example.ceritaku.view.authentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.R
import com.example.ceritaku.databinding.ActivityRegisterBinding
import com.example.ceritaku.remote.Result
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnregister.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }
    }

    private suspend fun register(){
        val name = binding.tvregisname.text.toString()
        val email = binding.tvregisemail.text.toString()
        val password = binding.tvregisterpassword.text.toString()

        viewModel.postRegister(name, email, password).observe(this){
            when(it){
                is Result.Sucess ->{
                    Log.d("loading",it.data.message)
                }
                is Result.Error ->{
                    Log.d("loading",it.error.toString())
                }
            }
        }

    }
}