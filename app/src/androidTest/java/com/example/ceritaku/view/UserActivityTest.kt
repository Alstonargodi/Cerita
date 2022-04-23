package com.example.ceritaku.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onIdle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.view.authentication.LoginActivity
import com.example.ceritaku.view.home.HomeFragment
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import com.example.ceritaku.view.upload.CameraFragment
import com.example.ceritaku.view.utils.IdlingConfig
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class UserActivityTest {

    @Before
    fun setup(){
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(IdlingConfig.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(IdlingConfig.countingIdlingResource)
    }

    fun login(){
        onView(withId(R.id.login_actvity))
            .check(matches(isDisplayed()))
        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
    }

    fun register(){
        onView(withId(R.id.login_actvity))
            .check(matches(isDisplayed()))

        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
    }

    @Test
    fun feedStoriestoDetail(){
        //todo idling pref data
        Thread.sleep(10000)
        onView(withId(R.id.tabLayoutHome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.viewPagerHome))
            .check(matches(isDisplayed()))


        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))

        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(1,click()))
        onView(withId(R.id.layout_detailstory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))
    }

    @Test
    fun mapsStoriesShow(){
        //todo idling pref data
        Thread.sleep(10000)
        onView(withId(R.id.tabLayoutHome))
            .check(matches(isDisplayed()))

        onView(withText("MAPS"))
            .perform(click())

        onView(withId(R.id.map))
            .check(matches(isDisplayed()))
    }


    @Test
    fun postNewStoriesCapture(){
        //todo idling pref data
        Thread.sleep(10000)
        onView(withId(R.id.add))
            .perform(click())
        onView(withId(R.id.btncapture))
            .perform(click())

        //todo idling after capture
        Thread.sleep(10000)
        onView(withId(R.id.layout_post_story))
            .check(matches(isDisplayed()))
        onView(withId(R.id.descarea))
            .perform(click())
            .perform(typeText("Robot ngepost"))
        onView(isRoot()).perform(closeSoftKeyboard())


        //todo idling after type
        Thread.sleep(10000)
        onView(withId(R.id.btnupload))
            .perform(click())


        //todo idling after upload
        Thread.sleep(15000)
        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))

        //todo idling pref data
        Thread.sleep(10000)
        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions
                .actionOnItem<RecyclerView.ViewHolder>(hasDescendant(withText("Robot ngepost")), click()))

        //todo idling pref detail
        Thread.sleep(10000)
        onView(withId(R.id.layout_detailstory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))
    }



}
