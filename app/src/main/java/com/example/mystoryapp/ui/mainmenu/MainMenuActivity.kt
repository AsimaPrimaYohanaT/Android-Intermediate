package com.example.mystoryapp.ui.mainmenu

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.databinding.ActivityMainMenuBinding
import com.example.mystoryapp.adapter.ListStoryAdapter
import com.example.mystoryapp.adapter.LoadingStateAdapter
import com.example.mystoryapp.ui.ViewModelFactory
import com.example.mystoryapp.ui.login.MainActivity
import com.example.mystoryapp.ui.maps.MapsActivity
import com.example.mystoryapp.ui.upload.UploadActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainMenuActivity : AppCompatActivity() {
    private lateinit var mainmenuViewModel: MainMenuViewModel
    private lateinit var binding: ActivityMainMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        setupView()
        setupViewModel()
        setupAction()

        binding.fabAdd.setOnClickListener{
            val toUpload = Intent(this@MainMenuActivity, UploadActivity::class.java)
            startActivity(toUpload)
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
        mainmenuViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore),this)
        )[MainMenuViewModel::class.java]

        mainmenuViewModel.getUser().observe(this, { user ->
            if (user.isLogin){
                binding.tvName.text = getString(R.string.greeting, user.name)
                getData(user.token)

            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })
    }

    private fun getData(token: String) {
        val adapter = ListStoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainmenuViewModel.story("Bearer " + token).observe(this, {
            adapter.submitData(lifecycle, it)
        })
    }

    private fun setupAction() {
        binding.fabLogout.setOnClickListener {
            mainmenuViewModel.logout()
        }

        binding.fabLocation.setOnClickListener{
            val intentLocation = Intent(this@MainMenuActivity, MapsActivity::class.java)
            startActivity(intentLocation)
        }
    }
}