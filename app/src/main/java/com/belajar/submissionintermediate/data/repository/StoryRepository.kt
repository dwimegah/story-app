package com.belajar.submissionintermediate.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.belajar.submissionintermediate.data.database.StoryDatabase
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.data.retrofit.ApiService
import com.belajar.submissionintermediate.view.story.StoryRemoteMediator

class StoryRepository(private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStories(token: String): LiveData<PagingData<StoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}