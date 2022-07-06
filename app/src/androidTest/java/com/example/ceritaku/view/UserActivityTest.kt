package com.example.ceritaku.view


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.ceritaku.MainActivity
import com.example.ceritaku.R
import com.example.ceritaku.view.home.adapter.StoryListAdapter
import com.example.ceritaku.view.utils.IdlingConfig
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class UserActivityTest {

    @get:Rule
    var activity = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setup(){
        IdlingRegistry.getInstance().register(IdlingConfig.countingIdlingResource)
    }

    @After
    fun tearDown(){
        IdlingRegistry.getInstance().unregister(IdlingConfig.countingIdlingResource)
    }

    @Test
    fun feedStoriesToDetail(){

        onView(withId(R.id.tabLayoutHome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.viewPagerHome))
            .check(matches(isDisplayed()))

        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))

        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))

        onView(withId(R.id.listHome_story))
            .perform(RecyclerViewActions.actionOnItemAtPosition<StoryListAdapter.ViewHolder>(1,click()))

        onView(withId(R.id.layout_detailstory))
            .check(matches(isDisplayed()))
        onView(withId(R.id.tvdetailstory_desc))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvdetailstory_back))
            .check(matches(isDisplayed()))

        onView(withId(R.id.tvdetailstory_back))
            .perform(click())

    }

    @Test
    fun postNewStoriesCapture(){

        onView(withId(R.id.add))
            .perform(click())
        onView(withId(R.id.btncapture))
            .perform(click())

        onView(withId(R.id.layout_post_story))
            .check(matches(isDisplayed()))

        onView(withId(R.id.descarea))
            .perform(click())
            .perform(typeText("Robot ngepost"))
        onView(isRoot()).perform(closeSoftKeyboard())

        onView(withId(R.id.btnupload))
            .perform(click())

        onView(withId(R.id.story_home))
            .check(matches(isDisplayed()))

        onView(withId(R.id.listHome_story))
            .check(matches(isDisplayed()))

    }


}
