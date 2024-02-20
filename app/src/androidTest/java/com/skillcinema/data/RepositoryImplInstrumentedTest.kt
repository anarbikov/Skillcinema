package com.skillcinema.data

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skillcinema.HiltTestActivity
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

    @get:Rule (order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule (order = 1)
    val activityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)
    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun clickHomeButton() {
        val activityScenario: ActivityScenario<MainActivity> = launchActivity()
        activityScenario.onActivity {
            val homeFragment: Fragment = it.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(HomeFragment::class.java.classLoader) as ClassLoader,
                HomeFragment::class.java.name
            )

            it.supportFragmentManager.beginTransaction()
                .add(android.R.id.content, homeFragment, "")
                .commitNow()

        }


//        val homeFragment = HomeFragment()

//        launchActivity<HiltTestActivity>()


//         launchFragmentInHiltContainer<HomeFragment>()
        Espresso.onView(withId(R.id.homeTextViewSkillCinema)).check(matches(ViewMatchers.withText("")))

    }

    inline fun <reified T : Fragment> launchFragmentInHiltContainer(
        fragmentArgs: Bundle? = null,
        @StyleRes themeResId: Int = R.style.Theme_SkillCinema,
        crossinline action: Fragment.() -> Unit = {}
    ) {
        val startActivityIntent = Intent.makeMainActivity(
            ComponentName(
                ApplicationProvider.getApplicationContext(),
                HiltTestActivity::class.java
            )
        ).putExtra(
            "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
            themeResId
        )

        ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
            val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
                Preconditions.checkNotNull(T::class.java.classLoader) as ClassLoader,
                T::class.java.name
            )
            fragment.arguments = fragmentArgs
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment, "")
                .commitNow()

            fragment.action()
        }
    }
}
