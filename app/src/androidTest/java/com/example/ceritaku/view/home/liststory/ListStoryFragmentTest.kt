package com.example.ceritaku.view.home.liststory

import android.widget.ListAdapter
import androidx.test.core.app.ActivityScenario
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.ceritaku.MainActivity
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.config.ApiConfig
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import okhttp3.mockwebserver.MockWebServer

@RunWith(AndroidJUnit4ClassRunner::class)
@MediumTest
class ListStoryFragmentTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setuo(){
        ActivityScenario.launch(MainActivity::class.java)
        mockWebServer.start(8080)
        ApiConfig.base_url = "http://127.0.0.1:8080/"
    }

    @Test
    fun showListStory(){
        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))
        onView(withId(R.id.story_list))
            .check(matches(isDisplayed()))
        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(0,click()))
        Thread.sleep(2000)
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))

    }


}