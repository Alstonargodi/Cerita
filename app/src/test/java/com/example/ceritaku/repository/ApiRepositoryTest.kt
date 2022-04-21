package com.example.ceritaku.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ApiRepositoryTest{


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCorotineRule = MainCoroutineRule()
    private lateinit var context : Context
    private lateinit var apiService: ApiService
    private lateinit var apiRepository : ApiRepository
    private lateinit var database : MediatorDatabase

    @Before
    fun setUp(){
        apiService = StoryFakeApiService()
        context = InstrumentationRegistry.getInstrumentation().targetContext
        database = MediatorDatabase.getDatabase(context)
        apiRepository = ApiRepository(database,apiService)
    }


    @Test
    fun loginSuccess() = runTest {
        val loginFake = DataDummy.genFakeLoginResponse()
        val login = apiService.postLogin("tset","tset")
        assertNotNull(login)
        assertEquals(loginFake.loginResult,login.loginResult)
    }



}