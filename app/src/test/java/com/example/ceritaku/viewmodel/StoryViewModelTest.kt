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
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock

    private lateinit var storyViewModel: StoryViewModel
    private var dummyListStory  = DataDummy.fakeListStoryResponse_Success()
    private var dummyNewStoryResponse = DataDummy.fakePostNewStories_Success()
    private var dummyToken = "Token"



    @Test
    fun `when get Feed Stories Success`() = runTest {
        val dummyPageStory = PagedTestDataSources.snapshot(dummyListStory.listStory)
        val storyTemp = MutableLiveData<PagingData<Story>>()
        storyTemp.value = dummyPageStory

        `when`(storyViewModel.getStoryList(dummyToken)).thenReturn(storyTemp)

        val storyActual = storyViewModel.getStoryList(dummyToken).getOrWaitValue()

        val storyActualPage = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = storyListCallback,
            mainDispatcher = mainCoroutineRule.dispatche,
            workerDispatcher = mainCoroutineRule.dispatche
        )
        storyActualPage.submitData(storyActual)

        advanceUntilIdle()
        Mockito.verify(storyViewModel).getStoryList(dummyToken)
        Assert.assertNotNull(storyActualPage.snapshot())
        Assert.assertTrue(storyActualPage.snapshot().isNotEmpty())
        Assert.assertEquals(dummyListStory.listStory.size, storyActualPage.snapshot().size)
        Assert.assertEquals(dummyListStory.listStory, storyActualPage.snapshot())
    }

    @Test
    fun `when Maps Stories Success`() = runTest {
        val storyExpected = MutableLiveData<MediatorResult<StoryResponse>>()
        storyExpected.value = MediatorResult.Sucess(dummyListStory)

        `when`(storyViewModel.getMapsStories(1,dummyToken)).thenReturn(storyExpected)

        val storyActual = storyViewModel.getMapsStories(1,dummyToken).getOrWaitValue()

        Mockito.verify(storyViewModel).getMapsStories(1,dummyToken)
        Assert.assertNotNull(storyActual)
        Assert.assertFalse(storyActual is MediatorResult.Error)
        Assert.assertTrue(storyActual is MediatorResult.Sucess)
        Assert.assertEquals(storyExpected.value,storyActual)
        Assert.assertEquals((storyExpected.value as MediatorResult.Sucess).data,(storyActual as MediatorResult.Sucess).data)
    }

    @Test
    fun `when Maps Stories Error`() = runTest {
        val storyExpected = MutableLiveData<MediatorResult<StoryResponse>>()
        storyExpected.value = MediatorResult.Error("fail")

        `when`(storyViewModel.getMapsStories(1,dummyToken)).thenReturn(storyExpected)

        val storyActual = storyViewModel.getMapsStories(1,dummyToken).getOrWaitValue()

        Mockito.verify(storyViewModel).getMapsStories(1,dummyToken)
        Assert.assertNotNull(storyActual)
        Assert.assertTrue(storyActual is MediatorResult.Error)
        Assert.assertFalse(storyActual is MediatorResult.Sucess)
        Assert.assertEquals(storyExpected.value,storyActual)
        Assert.assertEquals((storyExpected.value as MediatorResult.Error).error,(storyActual as MediatorResult.Error).error)
    }


    @Test
    fun `when Post New Stories`() = runTest{
        val desc = "description".toRequestBody("text/plain".toMediaType())
        val tempFile = File.createTempFile("prefix","suffix")
        val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            tempFile.name,
            requestFile
        )

        val newStoryExpected = MutableLiveData<MediatorResult<NewStoryResponse>>()
        newStoryExpected.value = MediatorResult.Sucess(dummyNewStoryResponse)

        `when`(storyViewModel.postStory(multiPart,desc,20F,20F,dummyToken)).thenReturn(newStoryExpected)

        val newStoryActual = storyViewModel.postStory(multiPart,desc,20F,20F,dummyToken).getOrWaitValue()

        Mockito.verify(storyViewModel).postStory(multiPart,desc,20F,20F,dummyToken)
        Assert.assertNotNull(newStoryActual)
        Assert.assertFalse(newStoryActual is MediatorResult.Error)
        Assert.assertTrue(newStoryActual is MediatorResult.Sucess)
        Assert.assertEquals(newStoryExpected.value,newStoryActual)
        Assert.assertEquals((newStoryExpected.value as MediatorResult.Sucess).data,(newStoryActual as MediatorResult.Sucess).data)

    }

}

class PagedTestDataSources private constructor(private val items: MediatorResult<List<Story>>) :
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