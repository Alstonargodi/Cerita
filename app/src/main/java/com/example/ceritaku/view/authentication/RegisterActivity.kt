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
import com.example.ceritaku.data.local.datastore.UserPrefrences
import com.example.ceritaku.data.local.datastore.dataStore
import com.example.ceritaku.databinding.ActivityRegisterBinding
import com.example.ceritaku.data.remote.utils.Result
import com.example.ceritaku.view.componen.PasswordBoxCustom
import com.example.ceritaku.viewmodel.AuthViewModel
import com.example.ceritaku.viewmodel.VModelFactory
import com.example.ceritaku.viewmodel.utils.PrefViewModelFactory
import com.example.ceritaku.viewmodel.utils.SettingPrefViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel : AuthViewModel by viewModels{
        VModelFactory.getInstance(this)
    }
    private lateinit var prefViewModel : SettingPrefViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefViewModel = ViewModelProvider(this,
            PrefViewModelFactory(UserPrefrences.getInstance(dataStore))
        )[SettingPrefViewModel::class.java]

        setEditTextPassword()

        binding.btnregister.setOnClickListener {
            lifecycleScope.launch {
                register()
            }
        }

        binding.btncancel.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finishAffinity()
        }
    }

    private fun boxChecker(): Boolean{
        val name = binding.tvregisname.text.toString()
        val email = binding.tvregisemail.text.toString()
        val password = binding.tvregisterpassword.text.toString()

        when {
            email.isEmpty() -> return true
            password.isEmpty() -> return true
            name.isEmpty() -> return true
            else -> (password.isNotEmpty() && email.isNotEmpty())
        }
            return false
    }

    private suspend fun register(){
        val name = binding.tvregisname.text.toString()
        val email = binding.tvregisemail.text.toString()
        val password = binding.tvregisterpassword.text.toString()

        if(!boxChecker()){
            viewModel.postRegister(name, email, password).observe(this){
                when(it){
                    is Result.Loading ->{
                        binding.pgbarregister.visibility = View.VISIBLE
                    }

                    is Result.Sucess ->{
                        binding.pgbarregister.visibility = View.GONE
                        showMessage(it.data.message + " and welcome")
                        lifecycleScope.launch {
                            loginRegister()
                        }
                    }

                    is Result.Error ->{
                        binding.pgbarregister.visibility = View.GONE
                        showMessage(it.error + "try again")
                    }

                }
            }
        }else{
            showMessage("fill textbox first")
        }
    }

    private suspend fun loginRegister(){
        val email = binding.tvregisemail.text.toString()
        val password = binding.tvregisterpassword.text.toString()

        viewModel.postLogin(email,password).observe(this){
            when(it){
                is Result.Loading->{
                    binding.pgbarregister.visibility = View.VISIBLE
                }
                is Result.Sucess->{
                    binding.pgbarregister.visibility = View.GONE
                    saveUserLogin(
                        it.data.loginResult.name,
                        it.data.loginResult.token,
                        true,
                    )
                    showMessage("welcome + ${it.data.loginResult.name}")
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
                is Result.Error->{
                    binding.pgbarregister.visibility = View.GONE
                    if (it.error == invalid){
                        showMessage("Invalid form")
                    }else{
                        showMessage(it.error)
                    }
                    Log.d(LoginActivity.tag, it.error)
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

    private fun setEditTextPassword(){

        binding.tvregisterpassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.tvregisterpassword.onItemClickDetail(object  : PasswordBoxCustom.SetHideCallBack{
            override fun setHideCallback(status: Boolean) {
                if (status){
                    binding.tvregisterpassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }else{
                    binding.tvregisterpassword.transformationMethod = null
                }
            }
        })
    }

    private fun showMessage(message : String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    companion object{
        const val invalid = "HTTP 400 Bad Requesttry again"
    }
}