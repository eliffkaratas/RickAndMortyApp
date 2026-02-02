package com.example.rickandmortyapp

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.rickandmortyapp.ui.characterdetail.CharacterDetailFragment
import com.example.rickandmortyapp.ui.model.CharacterArgs
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CharacterDetailFragmentTest {

    @Test
    fun shows_details_and_hides_url_when_null() {
        val scenario = ActivityScenario.launch(TestHostActivity::class.java)

        scenario.onActivity { activity ->
            val args = CharacterArgs(
                characterImage = null,
                characterName = "Alien Morty",
                characterStatus = "unknown",
                characterLocationName = "Unknown",
                characterLocationUrl = null,
                characterSpecies = "Alien",
                characterGender = "Male",
                characterCreated = "2017-11-04"
            )

            val fragment = CharacterDetailFragment().apply {
                arguments = bundleOf("detailArgs" to args)
            }

            fragment.show(activity.supportFragmentManager, "detail")
        }

        onView(withId(R.id.text_view_name)).inRoot(isDialog())
            .check(matches(withText("Alien Morty")))
        onView(withId(R.id.text_view_status)).inRoot(isDialog()).check(matches(withText("unknown")))
        onView(withId(R.id.text_view_location_name)).inRoot(isDialog())
            .check(matches(withText("Unknown")))
        onView(withId(R.id.text_view_species)).inRoot(isDialog()).check(matches(withText("Alien")))
        onView(withId(R.id.text_view_gender)).inRoot(isDialog()).check(matches(withText("Male")))
        onView(withId(R.id.text_view_created)).inRoot(isDialog())
            .check(matches(withText("2017-11-04")))

        // URL null => GONE olmalı
        onView(withId(R.id.text_view_location_url)).inRoot(isDialog())
            .check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun shows_url_when_not_null_and_close_dismisses() {
        val scenario = ActivityScenario.launch(TestHostActivity::class.java)

        scenario.onActivity { activity ->
            val args = CharacterArgs(
                characterImage = null,
                characterName = "Rick Sanchez",
                characterStatus = "Alive",
                characterLocationName = "Earth",
                characterLocationUrl = "https://example.com",
                characterSpecies = "Human",
                characterGender = "Male",
                characterCreated = "2017-11-04"
            )

            val fragment = CharacterDetailFragment().apply {
                arguments = bundleOf("detailArgs" to args)
            }

            fragment.show(activity.supportFragmentManager, "detail")
        }

        onView(withId(R.id.text_view_name)).check(matches(withText("Rick Sanchez")))

        onView(withId(R.id.text_view_location_url))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.text_view_location_url))
            .check(matches(withText("https://example.com")))

        onView(withId(R.id.toolbar)).perform(clickToolbarMenuItem(R.id.close))

        onView(withId(R.id.text_view_name)).check(doesNotExist())
    }
}

fun clickToolbarMenuItem(menuItemId: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> = isAssignableFrom(Toolbar::class.java)

        override fun getDescription(): String = "Click toolbar menu item: $menuItemId"

        override fun perform(uiController: UiController, view: View) {
            val toolbar = view as Toolbar
            toolbar.menu.performIdentifierAction(menuItemId, 0)
            uiController.loopMainThreadUntilIdle()
        }
    }
}