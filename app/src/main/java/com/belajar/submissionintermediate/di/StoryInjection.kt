package com.belajar.submissionintermediate.di

import android.content.Context
import com.belajar.submissionintermediate.data.database.StoryDatabase
import com.belajar.submissionintermediate.data.repository.StoryRepository
import com.belajar.submissionintermediate.data.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}