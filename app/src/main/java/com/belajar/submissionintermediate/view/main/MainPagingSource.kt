package com.belajar.submissionintermediate.view.main

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.data.response.Story
import com.belajar.submissionintermediate.data.retrofit.ApiService

class MainPagingSource(private val apiService: ApiService) : PagingSource<Int, Story>() {

    companion object {
        fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
            return PagingData.from(items)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            return LoadResult.Page(emptyList(), 0, 1)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return 0
    }
}