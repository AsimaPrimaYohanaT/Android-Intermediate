package com.example.mystoryapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.response.RegisterResponse
import com.example.mystoryapp.ui.MyEditText
import com.example.mystoryapp.ui.login.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var myEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        myEditText = binding.edPassword

        binding.btnToLogin.setOnClickListener{
            val toLogin = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(toLogin)
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


    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edUsername.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edUsername.error = "Masukkan username"
                }
                email.isEmpty() -> {
                    binding.edEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edPassword.error = "Masukkan password"
                }
                password.length < 8 -> {
                    binding.edPassword.error = "Password harus lebih dari 8 character"
                    return@setOnClickListener
                }
                else -> {
                    val client = ApiConfig.getApiService().register(email, name, password)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@RegisterActivity,"success", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@RegisterActivity,"error", Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "onFailure: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            Toast.makeText(this@RegisterActivity,t.message, Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "onFailure: ${t.message}")
                        }
                    })
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnToLogin, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                register,
                login
            )
            startDelay = 500
        }.start()
    }


}