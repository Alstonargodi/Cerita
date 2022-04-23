package com.example.ceritaku.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.local.mediator.story.StoryDao
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.view.utils.paging.StoryRemoteMediator

class FakeDao : StoryDao {
    var storyItem = mutableListOf<Story>()

    override suspend fun insertStory(story: List<Story>) {
    }

    override fun getAllStory(): PagingSource<Int, Story> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteStory() {
        TODO("Not yet implemented")
    }
}