package com.example.mystoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.ui.login.LoginViewModel
import com.example.mystoryapp.ui.mainmenu.MainMenuViewModel
import com.example.mystoryapp.ui.maps.MapsViewModel
import com.example.mystoryapp.ui.upload.UploadModel

class ViewModelFactory(private val pref: UserPreference, private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainMenuViewModel::class.java) -> {
                MainMenuViewModel(pref, Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref, Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(UploadModel::class.java) -> {
                UploadModel(pref, Injection.provideRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}