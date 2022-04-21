package com.example.ceritaku.view

import android.window.SplashScreen
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.view.start.splashscreen.SplashScreenActivity
import com.example.ceritaku.view.utils.EspressoIdlingResources
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class AllLargeTest {

    @Before
    fun setup(){
        ActivityScenario.launch(SplashScreenActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResources.countingIdlingResource)
    }

    @Test
    fun loginFirst(){
        onView(withId(R.id.splash_activity))
            .check(matches(isDisplayed()))
    }

}