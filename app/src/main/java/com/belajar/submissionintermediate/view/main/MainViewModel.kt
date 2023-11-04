package com.belajar.submissionintermediate.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.belajar.submissionintermediate.data.UserRepository
import com.belajar.submissionintermediate.data.pref.UserModel
import com.belajar.submissionintermediate.data.response.StoriesResponse
import com.belajar.submissionintermediate.data.response.Story
import com.belajar.submissionintermediate.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStories(token)
        Log.d(TAG, "client: $client")
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(call: Call<StoriesResponse>, response: Response<StoriesResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _stories.value = response.body()?.let { (it.listStory) }
                    Log.d(TAG, "onResponse: ${_stories.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    val listStory = null;
                    _isLoading.value = false
                    Log.e(TAG, "onResponse: $errorMessage")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}