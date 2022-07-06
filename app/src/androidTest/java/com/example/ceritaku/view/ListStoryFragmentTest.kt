package com.example.ceritaku.view

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.MediumTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.data.remote.config.ApiConfig
import com.example.ceritaku.utils.JsonConverter
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import com.example.ceritaku.view.utils.IdlingConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@MediumTest
class ListStoryFragmentTest {

    private val mockWebServer = MockWebServer()


    @get:Rule
    var activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup(){

        IdlingRegistry.getInstance().register(IdlingConfig.countingIdlingResource)

        mockWebServer.start(8080)
        ApiConfig.url = "http://127.0.0.1:8080/"

        val mockResponse = MockResponse()
            .setBody(JsonConverter.readFromJson("StoryResponse.json"))
        mockWebServer.enqueue(mockResponse)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingConfig.countingIdlingResource)
        mockWebServer.shutdown()
    }

    @Test
    fun listStoryToDetail(){
        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))
        onView(withId(R.id.story_list))
            .check(matches(isDisplayed()))
        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(0,click()))
        onView(withId(R.id.layout_detailstory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))
        onView(withText("pangeran")).check(matches(isDisplayed()))
        onView(withText("uhihih")).check(matches(isDisplayed()))
    }

    @Test
    fun getListStoryWhenSuccess(){
        val mockResponse = MockResponse()
            .setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.story_list))
            .check(matches(isDisplayed()))
        onView(withText("uhihih"))
            .check(matches(isDisplayed()))
        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("kapan ada waktu lagi"))))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(3,click()))
        onView(withId(R.id.layout_detailstory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))
        onView(withText("podashon")).check(matches(isDisplayed()))
        onView(withText("kapan ada waktu lagi")).check(matches(isDisplayed()))

    }

    @Test
    fun getListStoryByRemote(){
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)
        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))
            .check(matches(isDisplayed()))
            .perform(RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(hasDescendant(withText("kapan ada waktu lagi"))))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(3,click()))
        onView(withId(R.id.layout_detailstory)).check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_img)).check(matches(isDisplayed()))
        onView(withText("podashon")).check(matches(isDisplayed()))
        onView(withText("kapan ada waktu lagi")).check(matches(isDisplayed()))
    }


}