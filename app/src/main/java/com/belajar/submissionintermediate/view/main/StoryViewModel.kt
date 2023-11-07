package com.belajar.submissionintermediate.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.data.repository.StoryRepository
import com.belajar.submissionintermediate.di.StoryInjection

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories(token: String): LiveData<PagingData<StoryItem>> {
        val stories = repository.getStories(token).cachedIn(viewModelScope)
        if (stories != null) {
            _isLoading.value = false
        }
        return stories
    }
    companion object{
        private const val TAG = "StoryViewModel"
    }
}

class StoryModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(StoryInjection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}