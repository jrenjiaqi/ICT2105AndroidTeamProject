package com.example.ict2105project.Non_CRUD_Tests.admin


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ict2105project.LoginActivity
import com.example.ict2105project.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginAsAdminAttendanceAddEditDeleteExistsActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginAsManagerAttendanceAddEditDeleteExistsActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("adamadmin@precursor.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("adamadmin"), closeSoftKeyboard())

        val circularProgressButton = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton.perform(click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.menuAttendance), withContentDescription("Attendance"),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val button = onView(
            allOf(
                withId(R.id.addAttendanceButton),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val imageButton = onView(
            allOf(
                withId(R.id.editAttendanceButton),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val imageButton2 = onView(
            allOf(
                withId(R.id.deleteAttendanceButton),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}