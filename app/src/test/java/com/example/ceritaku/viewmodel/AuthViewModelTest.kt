package com.example.ceritaku.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.MainCoroutineRule
import com.example.ceritaku.utils.getOrWaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
//
//    @get:Rule
//    var instantExecutorRolule = InstantTaskExecutorRule()
//
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()
//
//    @Mock
//    private lateinit var authViewModel: AuthViewModel
//
//    @Test
//    fun postCorrectLogin() = runTest {
//        val dummyLoginResponse = DataDummy.genFakeLoginResponseSucces()
//        val story = MutableLiveData<MediatorResult<LoginResponse>>()
//        story.value = MediatorResult.Sucess(dummyLoginResponse)
//
//        `when`(authViewModel.postLogin(
//            "Budi",
//            "123456"
//
//        )).thenReturn(story)
//
//        val realLoginResponse = authViewModel.postLogin("Budi","123456").getOrWaitValue()
//
//        Assert.assertNotNull(realLoginResponse)
//        Assert.assertEquals(story.value,realLoginResponse)
//    }
//
//    @Test
//    fun postIncorrectLogin() = runTest {
//        val dummyLoginResponse = DataDummy.genFakeLoginResponseSucces()
//        val login = MutableLiveData<MediatorResult<LoginResponse>>()
//        login.value = MediatorResult.Error("error")
//
//        `when`(authViewModel.postLogin(
//            "Budi",
//            "123456"
//        )).thenReturn(login)
//
//        val realLoginResponse = authViewModel.postLogin("Budi","123456").getOrWaitValue()
//
//        Assert.assertTrue(realLoginResponse is MediatorResult.Error)
//        Assert.assertNotEquals(realLoginResponse,dummyLoginResponse)
//    }
//
//    @Test
//    fun postCorrectRegister() = runTest {
//
//    }


}