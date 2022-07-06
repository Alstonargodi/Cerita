package com.example.ceritaku.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.data.remote.response.login.LoginResponse
import com.example.ceritaku.data.remote.response.register.RegisterResponse
import com.example.ceritaku.data.remote.utils.MediatorResult
import com.example.ceritaku.utils.DataDummy
import com.example.ceritaku.utils.getOrWaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiRepository : ApiRepository
    private lateinit var authViewModel : AuthViewModel
    private val dummyResponseLogin = DataDummy.fakeLoginResponse_Success()
    private val dummyResponseRegister = DataDummy.fakeRegisterResponse_Success()

    private var dummyEmail = "usertesting@mail.com"
    private var dummyPassword = "testing12345"
    private var dummyName = "usertest"

    @Before
    fun setup(){
        authViewModel = AuthViewModel(apiRepository)
    }

    @Test
    fun `when login with fill form error = false`() = runTest {
        val loginExpected = MutableLiveData<MediatorResult<LoginResponse>>()
        loginExpected.value = MediatorResult.Sucess(dummyResponseLogin)

        `when`(authViewModel.postLogin(dummyEmail, dummyPassword)).thenReturn(loginExpected)

        val loginActual = authViewModel.postLogin(dummyEmail, dummyPassword).getOrWaitValue()

        Mockito.verify(apiRepository).postLogin(dummyEmail, dummyPassword)
        Assert.assertNotNull(loginActual)
        Assert.assertFalse(loginActual is MediatorResult.Error)
        Assert.assertTrue(loginActual is MediatorResult.Sucess)
        Assert.assertEquals((loginExpected.value as MediatorResult.Sucess).data,(loginActual as MediatorResult.Sucess).data)
    }

    @Test
    fun `when login with empty email error = true`() = runTest {
        val loginExpected = MutableLiveData<MediatorResult<LoginResponse>>()
        loginExpected.value = MediatorResult.Error("fail")

        `when`(authViewModel.postLogin("", dummyPassword)).thenReturn(loginExpected)

        val loginActual = authViewModel.postLogin("", dummyPassword).value

        Mockito.verify(apiRepository).postLogin("", dummyPassword)
        Assert.assertNotNull(loginActual)
        Assert.assertTrue(loginActual is MediatorResult.Error)
        Assert.assertFalse(loginActual is MediatorResult.Sucess)
        Assert.assertNotEquals(loginExpected,(loginActual as MediatorResult.Error))
    }

    @Test
    fun `when login with empty password error = true`() = runTest {
        val loginExpected = MutableLiveData<MediatorResult<LoginResponse>>()
        loginExpected.value = MediatorResult.Error("fail")

        `when`(authViewModel.postLogin(dummyName, "")).thenReturn(loginExpected)

        val loginActual = authViewModel.postLogin(dummyName, "").value

        Mockito.verify(apiRepository).postLogin(dummyName, "")
        Assert.assertNotNull(loginActual)
        Assert.assertTrue(loginActual is MediatorResult.Error)
        Assert.assertFalse(loginActual is MediatorResult.Sucess)
        Assert.assertNotEquals(loginExpected,(loginActual as MediatorResult.Error))
    }


    @Test
    fun `when Register with fill form error = false`() = runTest {
        val registerExpected = MutableLiveData<MediatorResult<RegisterResponse>>()
        registerExpected.value = MediatorResult.Sucess(dummyResponseRegister)

        `when`(authViewModel.postRegister(dummyName,dummyEmail, dummyPassword)).thenReturn(registerExpected)

        val registerActual = authViewModel.postRegister(dummyName,dummyEmail, dummyPassword).getOrWaitValue()

        Mockito.verify(apiRepository).postRegister(dummyName,dummyEmail, dummyPassword)
        Assert.assertNotNull(registerActual)
        Assert.assertFalse(registerActual is MediatorResult.Error)
        Assert.assertTrue(registerActual is MediatorResult.Sucess)
        Assert.assertEquals((registerExpected.value as MediatorResult.Sucess<RegisterResponse>).data,(registerActual as MediatorResult.Sucess).data)
    }

    @Test
    fun `when Register with empty name error = true`() = runTest {
        val registerExpected = MutableLiveData<MediatorResult<RegisterResponse>>()
        registerExpected.value = MediatorResult.Error("fail")

        `when`(authViewModel.postRegister("",dummyEmail, dummyPassword)).thenReturn(registerExpected)

        val registerActual = authViewModel.postRegister("",dummyEmail, dummyPassword).getOrWaitValue()

        Mockito.verify(apiRepository).postRegister("",dummyEmail, dummyPassword)
        Assert.assertNotNull(registerActual)
        Assert.assertTrue(registerActual is MediatorResult.Error)
        Assert.assertFalse(registerActual is MediatorResult.Sucess)
        Assert.assertNotEquals(registerExpected,(registerActual as MediatorResult.Error))
    }

    @Test
    fun `when Register with empty email error = true`() = runTest {
        val registerExpected = MutableLiveData<MediatorResult<RegisterResponse>>()
        registerExpected.value = MediatorResult.Error("fail")

        `when`(authViewModel.postRegister(dummyName,"", dummyPassword)).thenReturn(registerExpected)

        val registerActual = authViewModel.postRegister(dummyName,"", dummyPassword).getOrWaitValue()

        Mockito.verify(apiRepository).postRegister(dummyName,"", dummyPassword)
        Assert.assertNotNull(registerActual)
        Assert.assertTrue(registerActual is MediatorResult.Error)
        Assert.assertFalse(registerActual is MediatorResult.Sucess)
        Assert.assertNotEquals(registerExpected,(registerActual as MediatorResult.Error))
    }

    @Test
    fun `when Register with empty password error = true`() = runTest {
        val registerExpected = MutableLiveData<MediatorResult<RegisterResponse>>()
        registerExpected.value = MediatorResult.Error("fail")

        `when`(authViewModel.postRegister(dummyName,dummyEmail, "")).thenReturn(registerExpected)

        val registerActual = authViewModel.postRegister(dummyName,dummyEmail, "").getOrWaitValue()

        Mockito.verify(apiRepository).postRegister(dummyName,dummyEmail, "")
        Assert.assertNotNull(registerActual)
        Assert.assertTrue(registerActual is MediatorResult.Error)
        Assert.assertFalse(registerActual is MediatorResult.Sucess)
        Assert.assertNotEquals(registerExpected,(registerActual as MediatorResult.Error))
    }



}