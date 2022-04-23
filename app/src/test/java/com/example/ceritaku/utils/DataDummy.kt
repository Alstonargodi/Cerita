package com.example.ceritaku.utils

import android.util.Log
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.login.LoginResult
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.response.story.NewStoryResponse
import com.example.ceritaku.data.remote.response.story.Story
import com.example.ceritaku.data.remote.response.story.StoryResponse
import com.example.ceritaku.data.remote.utils.MediatorResult

object DataDummy {

    fun fakeLoginResponse_Success(): LoginResponse{
        return LoginResponse(
            false,
            LoginResult(
                "Test",
                "Test1244",
                "Test1234"
            ),
            "Success",
        )
    }

    fun fakeLoginResponse_Fail(): LoginResponse{
        return LoginResponse(
            true,
            LoginResult(
                "",
                "",
                ""
            ),
            "Invalid",
        )
    }

    fun fakeRegisterResponse_Success(): RegisterResponse{
        return RegisterResponse(
            false,
            "Success"
        )
    }

    fun fakeRegisterResponse_Fail(): RegisterResponse{
        return RegisterResponse(
            true,
            "Invalid"
        )
    }

    fun fakePostNewStories_Success() : NewStoryResponse{
        return NewStoryResponse(
            false,
            "Upload Success"
        )
    }

    fun fakePostNewStories_Fail() : NewStoryResponse{
        return NewStoryResponse(
            true,
            "Upload Fail"
        )
    }





    fun genFakeStory_Success(): List<Story>{
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..5){
            val tempData = Story(
            "story-Z6zjczpEWMht3Txn",
            "2022-04-19T13:24:38.736Z",
            "kapan ada waktu lagi ke $i",
            0F,
            0F,
            "budi",
            "https://i.ibb.co/mXb28k9/20210426144438-1.jpg",
            )
            items.add(tempData)
        }
        return items
    }

    fun genFakeStoryResponse(): MediatorResult<StoryResponse>{
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..5){
            val tempData = Story(
                "story-Z6zjczpEWMht3Txn",
                "2022-04-19T13:24:38.736Z",
                "kapan ada waktu lagi ke $i",
                0F,
                0F,
                "budi",
                "https://i.ibb.co/mXb28k9/20210426144438-1.jpg",
            )
            items.add(tempData)
        }
        return MediatorResult.Sucess(
            StoryResponse(
                false,
                items,
                "Succesfuly"
            )
        )
    }



}