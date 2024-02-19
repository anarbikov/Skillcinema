package com.skillcinema.data

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.EmptyFragmentActivity.Companion.THEME_EXTRAS_BUNDLE_KEY
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skillcinema.HiltTestActivity
import com.skillcinema.R
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

        launchActivity<HiltTestActivity>()


        launchFragmentInHiltContainer<HomeFragment>()
        onView(withId(R.id.homeTextViewSkillCinema)).perform(click()).check(matches(isDisplayed()))

    }

    private inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        themeResId: Int = R.style.Theme_SkillCinema,
        fragmentFactory: FragmentFactory? = null,
        crossinline action: T.() -> Unit = {}
    ) {
        val mainActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra(THEME_EXTRAS_BUNDLE_KEY, themeResId)

        ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { activity ->
            fragmentFactory?.let { activity.supportFragmentManager.fragmentFactory = it }
            val fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                checkNotNull(T::class.java.classLoader),
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            (fragment as T).action()
        }
    }
}
