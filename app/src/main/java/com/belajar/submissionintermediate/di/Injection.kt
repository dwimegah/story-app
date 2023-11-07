package com.belajar.submissionintermediate.di

import android.content.Context
import com.belajar.submissionintermediate.data.repository.UserRepository
import com.belajar.submissionintermediate.data.pref.UserPreference
import com.belajar.submissionintermediate.data.pref.dataStore
import com.belajar.submissionintermediate.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
}