package com.example.ceritaku.view.utils.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.service.ApiService

class StoryPagingSource(private val apiService: ApiService, val auth : String)
    : PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)
                ?:
            anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
       return try {
           val position = params.key ?: initial_page
           val responData = apiService.getStoriesList(
               position,params.loadSize,auth
           )
           Log.d("pagingsource",params.loadSize.toString())
           LoadResult.Page(
               data = responData.listStory,
               prevKey = if (position == initial_page) null else position - 1,
               nextKey = if (responData.listStory.isNullOrEmpty()) null else position + 1
           )
       } catch (e : Exception){
           Log.d("pagingsource",e.toString())
           return LoadResult.Error(e)
       }
    }

    private companion object{
        const val initial_page = 1
    }
}