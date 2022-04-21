package com.example.ceritaku.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.ceritaku.BuildConfig
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.MainCoroutineRule
import com.example.ceritaku.utils.getOrWaitValue
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var storyViewModel: StoryViewModel

    @Test
    fun `get Feed Stories when Success`() = runTest {
        val dummyStories = DataDummy.genFakeStory_Success()
        val data = PagedTestDataSources.snapshot(dummyStories)
        val story = MutableLiveData<PagingData<Story>>()
        story.value = data

        `when`(storyViewModel.getStoryList(BuildConfig.TEST_TOKEN)).thenReturn(story)

        val getRealStories = storyViewModel.getStoryList(BuildConfig.TEST_TOKEN).getOrWaitValue()

        val realStories = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = storyListCallback,
            mainDispatcher = mainCoroutineRule.dispatche,
            workerDispatcher = mainCoroutineRule.dispatche
        )

        realStories.submitData(getRealStories)

        advanceUntilIdle()

        Mockito.verify(storyViewModel).getStoryList(BuildConfig.TEST_TOKEN)
        Assert.assertNotNull(realStories.snapshot())
        Assert.assertTrue(realStories.snapshot().isNotEmpty())
        Assert.assertEquals(dummyStories.size, realStories.snapshot().size)
        Assert.assertEquals(dummyStories[0].name, realStories.snapshot()[0]?.name)
    }

    @Test
    fun `get Feed Stories when Error`() = runTest {
        val dummyStories = DataDummy.genFakeStory_Success()
        val data = PagedTestDataSources.snapshot(dummyStories)
        val story = MutableLiveData<PagingData<Story>>()
        story.value = data

        `when`(storyViewModel.getStoryList("a")).thenReturn(story)

        val getRealStories = storyViewModel.getStoryList("a").getOrWaitValue()

        val realStories = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = storyListCallback,
            mainDispatcher = mainCoroutineRule.dispatche,
            workerDispatcher = mainCoroutineRule.dispatche
        )

        realStories.submitData(getRealStories)

        Assert.assertTrue(realStories.snapshot().isNotEmpty())
    }

    @Test
    fun `get Maps Stories when Success`() = runTest {
        val dummyResponse = DataDummy.genFakeStoryResponse()
        val stories = MutableLiveData<MediatorResult<StoryResponse>>()
        stories.value = dummyResponse

        `when`(storyViewModel.getMapsStories(1,BuildConfig.TEST_TOKEN)).thenReturn(stories)

        val realMapsStoriesRespon = storyViewModel.getMapsStories(1,BuildConfig.TEST_TOKEN).getOrWaitValue()


        Assert.assertEquals(stories.value,realMapsStoriesRespon)

    }
}

class PagedTestDataSources private constructor(private val items: List<Story>) :
    PagingSource<Int, LiveData<List<Story>>>() {
    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
        return 0
    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
        return LoadResult.Page(emptyList(), 0 , 1)
    }
}

val storyListCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}