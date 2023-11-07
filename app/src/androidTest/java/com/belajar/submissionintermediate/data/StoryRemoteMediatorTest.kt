package com.belajar.submissionintermediate.data

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.belajar.submissionintermediate.data.database.StoryDatabase
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.data.response.CommonResponse
import com.belajar.submissionintermediate.data.response.LoginResponse
import com.belajar.submissionintermediate.data.response.StoriesResponse
import com.belajar.submissionintermediate.data.response.Story
import com.belajar.submissionintermediate.data.retrofit.ApiService
import com.belajar.submissionintermediate.view.story.StoryRemoteMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    private var dummyToken = "token"

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            dummyToken,
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, StoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override fun postLogin(email: String, password: String): Call<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun register(name: String, email: String, password: String): Call<CommonResponse> {
        TODO("Not yet implemented")
    }

    override fun getStories(token: String): Call<StoriesResponse> {
        TODO("Not yet implemented")
    }

    override fun postStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Call<CommonResponse> {
        TODO("Not yet implemented")
    }

    override fun getStoriesWithLocation(token: String, location: Int): Call<StoriesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesWithPaging(token: String, page: Int, size: Int): StoriesResponse {
        val items: MutableList<StoryItem> = arrayListOf()
        for(i in 0..100){
            val story = StoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        val startIndex = (page - 1) * size
        val endIndex = startIndex + size
        val sublist = if (startIndex < items.size) {
            items.subList(startIndex, endIndex.coerceAtMost(items.size))
        } else {
            emptyList()
        }
        val listStoryItem = sublist.map {
            Story(
                it.createdAt!!,
                it.description!!,
                it.id,
                it.name!!,
                it.photoUrl!!,
                it.lat,
                it.lon
            )
        }.toList()
        return StoriesResponse(false, listStoryItem, "success")
    }
}