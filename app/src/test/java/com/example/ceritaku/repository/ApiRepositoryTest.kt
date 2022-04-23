package com.example.ceritaku.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ceritaku.BuildConfig
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.login.LoginResult
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.FakeApiService
import com.example.ceritaku.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class ApiRepositoryTest{

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @InjectMocks
    private lateinit var apiService: ApiService
    private lateinit var apiRepository : ApiRepository
    private lateinit var database : MediatorDatabase


    @Before
    fun setUp(){
        apiService = FakeApiService()
        database = mock(MediatorDatabase::class.java)
        apiRepository = ApiRepository(database,apiService)
        MockitoAnnotations.initMocks(this)
    }


    @Test
    fun `when login with empty form return error = true`() = runBlockingTest {
        val loginFake = DataDummy.genFakeLoginResponseFail()
        val loginTrue = apiService.postLogin("email","password")

        assertEquals(loginFake,loginTrue)
    }


    fun `when login with empty email`() = runBlockingTest {
        val loginFake = DataDummy.genFakeLoginResponseFail()
        val loginTrue = apiRepository.postLogin("aji@gmail.com","123456")


        assertEquals(LoginResponse(true,LoginResult("","",""),null),loginTrue)
    }


    fun `get stories maps`() = runTest {
        val loginFake = DataDummy.genFakeLoginResponseFail()
        val loginTrue = apiRepository.getLocStories(1,BuildConfig.TEST_TOKEN)

        assertEquals(loginFake,loginTrue.value)
    }





}