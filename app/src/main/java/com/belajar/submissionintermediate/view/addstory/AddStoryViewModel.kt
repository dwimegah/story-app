package com.belajar.submissionintermediate.view.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belajar.submissionintermediate.data.response.CommonResponse
import com.belajar.submissionintermediate.data.retrofit.ApiConfig
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel : ViewModel() {
    private val _story = MutableLiveData<CommonResponse>()
    val story: LiveData<CommonResponse> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun postStory(token: String, photo: MultipartBody.Part, description: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postStory(token, photo, description)
        Log.d(TAG, "postStory: $client")
        client.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _story.value = response.body()
                    Log.d(TAG, "onResponse: ${_story.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    _isLoading.value = false
                    _story.value = CommonResponse(true, errorMessage.toString())
                    Log.e(TAG, "onResponse: $errorMessage")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _isLoading.value = false
                _story.value = CommonResponse( true, t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "StoryViewModel"
    }
}