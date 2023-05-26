package com.example.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.mystoryapp.api.ApiService
import com.example.mystoryapp.database.StoryDatabase
import com.example.mystoryapp.response.ListStoryItem

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}