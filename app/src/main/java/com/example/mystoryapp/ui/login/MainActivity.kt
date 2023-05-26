package com.example.mystoryapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.data.UserModel
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.response.LoginResponse
import com.example.mystoryapp.ui.MyEditText
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.mainmenu.MainMenuActivity
import com.example.mystoryapp.ui.register.RegisterActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: UserModel
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
        playAnimation()

        myEditText = binding.edPassword

        binding.btnToRegister.setOnClickListener{
            val toRegister = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(toRegister)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[LoginViewModel::class.java]

        loginViewModel.getUser().observe(this, { user ->
            if(user.isLogin){
                startActivity(Intent(this@MainActivity, MainMenuActivity::class.java))
            }
        })
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edPassword.error = "Masukkan password"
                }
                password.length < 8  -> {
                    binding.edPassword.error = "Password harus lebih dari 8 character"
                }
                else -> {
                    val client = ApiConfig.getApiService().login(email, password)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            if (response.isSuccessful) {
                                loginViewModel.login()
                                val user = response.body()?.loginResult as UserModel
                                loginViewModel.saveUser(UserModel(user.name,user.userId,user.token,true))
                                Toast.makeText(this@MainActivity,"success", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity,"error", Toast.LENGTH_SHORT).show()
                                Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@MainActivity,t.message, Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val register = ObjectAnimator.ofFloat(binding.btnToRegister, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                login,
                register)
            startDelay = 500
        }.start()
    }

}