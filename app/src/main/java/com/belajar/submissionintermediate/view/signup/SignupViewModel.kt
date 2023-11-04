package com.belajar.submissionintermediate.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.belajar.submissionintermediate.data.response.CommonResponse
import com.belajar.submissionintermediate.data.retrofit.ApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _signup = MutableLiveData<CommonResponse>()
    val signup: LiveData<CommonResponse> = _signup

    private val _isLoadingSignup = MutableLiveData<Boolean>()
    val isLoadingSignup: LiveData<Boolean> = _isLoadingSignup

    fun register(name: String, email: String, password: String) {
        _isLoadingSignup.value = true
        val client = ApiConfig.getApiService().register(name, email, password)
        Log.d(TAG, "register: $client")
        client.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>) {
                if (response.isSuccessful) {
                    _isLoadingSignup.value = false
                    _signup.value = response.body()
                    Log.d(TAG, "onResponse: ${_signup.value}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = errorBody?.let { JSONObject(it).getString("message") }
                    _isLoadingSignup.value = false
                    _signup.value = CommonResponse(true, errorMessage.toString())
                    Log.e(TAG, "onResponse: $errorMessage")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _isLoadingSignup.value = false
                _signup.value = CommonResponse(true, t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}