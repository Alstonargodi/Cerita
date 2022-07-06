package com.example.ceritaku.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.ceritaku.data.remote.repository.ApiRepository
import com.example.ceritaku.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingPrefViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiRepository: ApiRepository
    private lateinit var settingViewModel : SettingPrefViewModel
    private var prefTokenDummy = DataDummy.fakeTokenPrefrences()
    private var prefNameDummy = DataDummy.fakeNamePrefrences()
    private var prefOnboardDummy = DataDummy.fakeOnBoardStatusPrefrences()


    @Before
    fun setup(){
        settingViewModel = SettingPrefViewModel(apiRepository)
    }

    @Test
    fun `get user token when success`() = runTest {
        val tokenExpected = MutableLiveData<String>()
        tokenExpected.value = prefTokenDummy
        Mockito.`when`(settingViewModel.getUserToken()).thenReturn(tokenExpected)
        val tokenActual = settingViewModel.getUserToken().value
        Mockito.verify(apiRepository).getUserToken()
        Assert.assertEquals(tokenExpected.value,tokenActual)
    }

    @Test
    fun `get user name when success`() = runTest {
        val nameExpected = MutableLiveData<String>()
        nameExpected.value = prefNameDummy
        Mockito.`when`(settingViewModel.getUserName()).thenReturn(nameExpected)
        val nameActual = settingViewModel.getUserName().value
        Mockito.verify(apiRepository).getUserName()
        Assert.assertEquals(nameExpected.value,nameActual)
    }

    @Test
    fun `get user onboard when success`() = runTest {
        val onboardExpected = MutableLiveData<Boolean>()
        onboardExpected.value = prefOnboardDummy
        Mockito.`when`(settingViewModel.getOnBoardStatus()).thenReturn(onboardExpected)
        val onboardActual = settingViewModel.getOnBoardStatus().value
        Mockito.verify(apiRepository).getOnBoardStatus()
        if (onboardActual != null) {
            Assert.assertTrue(onboardActual)
        }
        Assert.assertEquals(onboardExpected.value,onboardActual)
    }

}