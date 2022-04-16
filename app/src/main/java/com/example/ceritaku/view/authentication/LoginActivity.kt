package com.example.ceritaku.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.local.UserPrefrences
import com.example.ceritaku.data.local.dataStore
import com.example.ceritaku.databinding.ActivityLoginBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.view.componen.PasswordBoxCustom
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance()
    }
    private lateinit var prefViewModel : SettingPrefViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        prefViewModel = ViewModelProvider(this,
            PrefViewModelFactory(UserPrefrences.getInstance(dataStore))
        )[SettingPrefViewModel::class.java]


        setEditTextPassword()


        binding.btnlogin.setOnClickListener {
            sessionChecker()
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

    }


    private fun boxChecker(): Boolean{
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString()

        when {
            email.isEmpty() -> {
                showMessage("need email")
                return true
            }
            password.isEmpty() -> {
                showMessage("need password")
                return true
            }
            else -> (password.isNotEmpty() && email.isNotEmpty())
        }
            return false

    }

    private fun sessionChecker(){
        prefViewModel.getUserName().observe(this){ respon->
            if (respon.isNullOrEmpty()){
                if (boxChecker()){
                    showMessage(getString(R.string.Login_error))
                }else{
                    lifecycleScope.launch { login() }
                }
            }else{
                startActivity(
                    Intent(this,MainActivity::class.java)
                )
                finishAffinity()
            }
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
                        onBoard = true,
                    )
                    showMessage("welcome + ${it.data.loginResult.name}")
                    nextPageSucess()
                }
                is Result.Error->{
                    binding.pgbarlogin.visibility = View.GONE
                    if (it.error == invalid){
                        showMessage(getString(R.string.Login_formerror))
                    }else{
                        showMessage(it.error)
                    }
                    Log.d(tag, it.error)
                }
            }
        }
    }

    private fun saveUserLogin(name : String, token : String, onBoard : Boolean){
        prefViewModel.saveThemeSetting(
            onBoard,
            name,
            token
        )
    }

    private fun nextPageSucess(){
        startActivity(Intent(this,MainActivity::class.java))
        finishAffinity()
    }

    private fun showMessage(message : String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }



    private fun setEditTextPassword(){
        binding.password.apply {
            transformationMethod = PasswordTransformationMethod.getInstance()
            onItemClickDetail(object  : PasswordBoxCustom.SetHideCallBack{
                override fun setHideCallback(status: Boolean) {
                    if (status){
                        binding.password.transformationMethod = PasswordTransformationMethod.getInstance()
                    }else{
                        binding.password.transformationMethod = null
                    }
                }
            })
        }
    }



    companion object{
        const val tag = "LoginActivity"
        const val invalid = "HTTP 400 Bad Request"
    }
}