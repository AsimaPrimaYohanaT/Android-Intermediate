package com.example.mystoryapp.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.UserModel
import com.example.mystoryapp.data.UserPreference

class UploadModel(private val pref: UserPreference, private val storyRepository: StoryRepository): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }
}