package com.example.mystoryapp.ui.maps

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.api.ApiConfig
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.UserModel
import com.example.mystoryapp.data.UserPreference
import com.example.mystoryapp.response.ListStoryItem
import com.example.mystoryapp.response.ListStoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference, private val storyRepository: StoryRepository): ViewModel() {

    private val listStory = MutableLiveData<ArrayList<ListStoryItem>?>()

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun story() = listStory

    fun setstory(token: String){
        val client = ApiConfig.getApiService().getStoryLocation("Bearer $token",1)
        client.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                if(response.isSuccessful){
                    listStory.postValue(response.body()?.listStory as ArrayList<ListStoryItem> )
                }else {
                    listStory.postValue(null )
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}