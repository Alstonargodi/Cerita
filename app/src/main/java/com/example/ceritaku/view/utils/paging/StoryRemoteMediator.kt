package com.example.ceritaku.view.utils.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.local.mediator.keys.RemoteKeys
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.view.utils.wrapperIdling

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database : MediatorDatabase,
    private val apiService: ApiService,
    private val auth : String
): RemoteMediator<Int,Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Story>)
    : MediatorResult {
        wrapperIdling {
            val page = when(loadType){
                LoadType.REFRESH->{
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initial_page
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }

            try {
                val responseData = apiService.getStoriesList(
                    page = page,
                    size = state.config.pageSize,
                    auth = auth
                ).listStory

                val endOfPaginationReached = responseData.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        database.keysDao().deleteRemoteKeys()
                        database.storyDao().deleteStory()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = responseData.map {
                        RemoteKeys(
                            id = it.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    database.keysDao().insertAll(keys)
                    database.storyDao().insertStory(responseData)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: Exception) {
                return MediatorResult.Error(exception)
            }
        }

    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.keysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.keysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.keysDao().getRemoteKeysId(id)
            }
        }
    }

    private companion object{
        const val initial_page = 1
    }


}