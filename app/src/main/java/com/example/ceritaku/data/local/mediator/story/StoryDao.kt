package com.example.ceritaku.data.local.mediator.story

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ceritaku.data.remote.response.story.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story : List<Story>)

    @Query("SELECT * FROM Story")
    fun getAllStory(): PagingSource<Int,Story>

    @Query("DELETE FROM Story")
    suspend fun deleteStory()
}