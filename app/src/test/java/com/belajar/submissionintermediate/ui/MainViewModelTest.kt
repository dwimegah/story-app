package com.belajar.submissionintermediate.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.belajar.submissionintermediate.DataDummy
import com.belajar.submissionintermediate.MainDispatcherRule
import com.belajar.submissionintermediate.data.repository.StoryRepository
import com.belajar.submissionintermediate.data.database.StoryItem
import com.belajar.submissionintermediate.getOrAwaitValue
import com.belajar.submissionintermediate.view.main.MainAdapter
import com.belajar.submissionintermediate.view.main.MainPagingSource
import com.belajar.submissionintermediate.view.main.StoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mdr= MainDispatcherRule ()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private var dummy_token = "token"

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryItem> = MainPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories(dummy_token)).thenReturn(expectedStory)

        val storyViewModel = StoryViewModel(storyRepository)
        val aqtualStory: PagingData<StoryItem> = storyViewModel.getStories(dummy_token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(aqtualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryItem>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStories(dummy_token)).thenReturn(expectedQuote)

        val mainViewModel = StoryViewModel(storyRepository)
        val actualQuote: PagingData<StoryItem> = mainViewModel.getStories(dummy_token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MainAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}