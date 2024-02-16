package com.skillcinema.data

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skillcinema.R
import com.skillcinema.ui.MainActivity
import com.skillcinema.ui.home.HomeFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class RepositoryImplInstrumentedTest {
    //    @JvmField
//    @Rule
//    val activityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun clickHomeButton() {
//        val homeFragment = HomeFragment()


        launchActivity<MainActivity>()
        launchFragmentInContainer<HomeFragment>()
        onView(withId(R.id.homeTextViewSkillCinema)).perform(click()).check(matches(isDisplayed()))

    }


}