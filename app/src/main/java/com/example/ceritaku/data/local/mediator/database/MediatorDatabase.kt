package com.example.ceritaku.data.local.mediator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ceritaku.data.local.mediator.keys.RemoteDao
import com.example.ceritaku.data.local.mediator.keys.RemoteKeys
import com.example.ceritaku.data.local.mediator.story.StoryDao
import com.example.ceritaku.data.remote.response.story.Story

@Database(entities = [Story::class,RemoteKeys::class]
    , version = 1, exportSchema = false)
abstract class MediatorDatabase : RoomDatabase(){
    abstract fun storyDao() : StoryDao
    abstract fun keysDao() : RemoteDao

    companion object{
        @Volatile
        private var INSTANCE: MediatorDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MediatorDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MediatorDatabase::class.java, "StoryDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}