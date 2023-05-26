package com.example.mystoryapp.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.UserModel
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.response.ListStoryItem
import kotlinx.coroutines.launch

class MainMenuViewModel(private val pref: UserPreference, private val storyRepository: StoryRepository) : ViewModel() {

    fun story(token: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

}