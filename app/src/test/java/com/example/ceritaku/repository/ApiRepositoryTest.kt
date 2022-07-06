package com.example.ceritaku.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ceritaku.data.local.datastore.UserPreferences
import com.example.ceritaku.data.local.mediator.database.MediatorDatabase
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.service.ApiService
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.FakeApiService
import com.example.ceritaku.utils.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ApiRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var userPreferences: UserPreferences
    private lateinit var apiService : ApiService
    private lateinit var apiRepository : ApiRepository
    private lateinit var database : MediatorDatabase


    @Before
    fun setUp(){
        apiService = FakeApiService()
        userPreferences = mock(UserPreferences::class.java)
        database = mock(MediatorDatabase::class.java)
        apiRepository = ApiRepository(database,apiService,userPreferences)
    }


    @Test
    fun `when login with empty form error = true`() = runTest {
        val loginExpected = DataDummy.fakeLoginResponse_Success()
        val loginActual = apiService.postLogin("","")

        assertTrue(loginActual.error)
        assertNotEquals(loginExpected.error,loginActual.error)
        assertNotEquals(loginExpected.message,loginActual.message)
    }

    @Test
    fun `when login with empty email error = true`() = runTest {
        val loginExpected = DataDummy.fakeLoginResponse_Success()
        val loginActual = apiService.postLogin("email","")

        assertTrue(loginActual.error)
        assertNotEquals(loginExpected.error,loginActual.error)
        assertNotEquals(loginExpected.message,loginActual.message)
    }

    @Test
    fun `when login with empty password error = true`() = runTest {
        val loginExpected = DataDummy.fakeLoginResponse_Success()
        val loginActual = apiService.postLogin("","password")

        assertTrue(loginActual.error)
        assertNotEquals(loginExpected.error,loginActual.error)
        assertNotEquals(loginExpected.message,loginActual.message)
    }

    @Test
    fun `when login with fill form error = false`() = runTest {
        val loginExpected = DataDummy.fakeLoginResponse_Success()
        val loginActual = apiService.postLogin("email","password")

        assertFalse(loginActual.error)
        assertEquals(loginExpected.error,loginActual.error)
        assertEquals(loginExpected.message,loginActual.message)
    }

    @Test
    fun `when Register with empty form error = true`() = runTest {
        val registerExpected = DataDummy.fakeRegisterResponse_Success()
        val registerActual = apiService.postRegister("","","")

        assertTrue(registerActual.error)
        assertNotEquals(registerExpected.error,registerActual.error)
        assertNotEquals(registerExpected.message,registerActual.message)
    }

    @Test
    fun `when Register with name form error = true`() = runTest {
        val registerExpected = DataDummy.fakeRegisterResponse_Success()
        val registerActual = apiService.postRegister("name","","")

        assertTrue(registerActual.error)
        assertNotEquals(registerExpected.error,registerActual.error)
        assertNotEquals(registerExpected.message,registerActual.message)
    }

    @Test
    fun `when Register with email form error = true`() = runTest {
        val registerExpected = DataDummy.fakeRegisterResponse_Success()
        val registerActual = apiService.postRegister("","email","")

        assertTrue(registerActual.error)
        assertNotEquals(registerExpected.error,registerActual.error)
        assertNotEquals(registerExpected.message,registerActual.message)
    }

    @Test
    fun `when Register with fill form error = false`() = runTest {
        val registerExpected = DataDummy.fakeRegisterResponse_Success()
        val registerActual = apiService.postRegister("name","email","password")

        assertFalse(registerActual.error)
        assertEquals(registerExpected.error,registerActual.error)
        assertEquals(registerExpected.message,registerActual.message)
    }


    @Test
    fun `when Post Story Fill form`() = runTest {
        val desc = "description"
            .toRequestBody("text/plain".toMediaType())
        val tempFile = File.createTempFile("prefix","suffix")
        val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            tempFile.name,
            requestFile
        )

        val postExpected = DataDummy.fakePostNewStories_Success()
        val postActual = apiService.postStory(multiPart,desc,200F,200F,"asdadasd")


        assertFalse(postActual.error)
        assertEquals(postExpected.error,postActual.error)
        assertEquals(postExpected.message,postActual.message)

    }

    @Test
    fun `when get List Stories with Token`() = runTest {
        val listExpected = DataDummy.fakeListStoryResponse_Success()
        val listActual = apiService.getStoriesList(1,10,"token")

        assertFalse(listActual.error)
        assertEquals(listExpected.error,listExpected.error)
        assertEquals(listExpected.message,listActual.message)
    }

    @Test
    fun `when get List Stories without Token`() = runTest {
        val listExpected = DataDummy.fakeListStoryResponse_Fail()
        val listActual = apiService.getStoriesList(1,10,"")

        assertTrue(listActual.error)
        assertEquals(listExpected.error,listExpected.error)
        assertEquals(listExpected.message,listActual.message)
    }

    @Test
    fun `when get maps Stories with Token`() = runTest {
        val mapsExpected = DataDummy.fakeListStoryResponse_Success()
        val mapsActual = apiService.getMapsStories(1,"token")

        assertFalse(mapsActual.error)
        assertEquals(mapsExpected.error,mapsExpected.error)
        assertEquals(mapsExpected.message,mapsActual.message)
    }

    @Test
    fun `when get maps Stories without Token`() = runTest {
        val mapsExpected = DataDummy.fakeListStoryResponse_Fail()
        val mapsActual = apiService.getMapsStories(1,"")

        assertTrue(mapsActual.error)
        assertEquals(mapsExpected.error,mapsExpected.error)
        assertEquals(mapsExpected.message,mapsActual.message)
    }

    @Test
    fun `when get token preferences`() = runTest {
        val tokenExpected = DataDummy.fakeTokenPrefrences()
        val tokenActual = userPreferences.getUserToken()
        assertNotEquals(tokenExpected,tokenActual)
    }

    @Test
    fun `when get name preferences`() = runTest{
        val tokenExpected = DataDummy.fakeNamePrefrences()
        val tokenActual = userPreferences.getUserName()
        assertNotEquals(tokenExpected,tokenActual)
    }

    @Test
    fun `when get onboard preferences`() = runTest{
        val tokenExpected = DataDummy.fakeOnBoardStatusPrefrences()
        val tokenActual = userPreferences.getUserName()
        assertNotEquals(tokenExpected,tokenActual)
    }

}