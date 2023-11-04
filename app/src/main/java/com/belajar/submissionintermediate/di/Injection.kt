package com.belajar.submissionintermediate.di

import android.content.Context
import com.belajar.submissionintermediate.data.UserRepository
import com.belajar.submissionintermediate.data.pref.UserPreference
import com.belajar.submissionintermediate.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}