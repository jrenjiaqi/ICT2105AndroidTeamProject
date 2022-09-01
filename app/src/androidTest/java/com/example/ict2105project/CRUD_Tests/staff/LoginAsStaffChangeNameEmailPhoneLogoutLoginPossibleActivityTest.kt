package com.example.ict2105project.CRUD_Tests.staff


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
class LoginAsStaffChangeNameEmailPhoneLogoutLoginPossibleActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginAsStaffChangeNameEmailPhoneLogoutLoginPossibleActivityTest() {
        val textInputEditText = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText("stevenstaff@precursor.com"), closeSoftKeyboard())

        val textInputEditText2 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText2.perform(replaceText("stevenstaff"), closeSoftKeyboard())

        val circularProgressButton = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton.perform(click())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.menuProfile), withContentDescription("Profile"),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val circularProgressButton2 = onView(
            allOf(
                withId(R.id.editProfileButton)
            )
        )
        circularProgressButton2.perform(scrollTo(), click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.nameEditText)
            )
        )
        appCompatEditText.perform(scrollTo(), replaceText("Steven Staph"))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.nameEditText),
                isDisplayed()
            )
        )
        appCompatEditText2.perform(closeSoftKeyboard())

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.phoneNumberEditText)
            )
        )
        appCompatEditText3.perform(scrollTo(), replaceText("22222221"))

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.phoneNumberEditText),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(closeSoftKeyboard())

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.emailEditText)
            )
        )
        appCompatEditText5.perform(scrollTo(), click())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.emailEditText)
            )
        )
        appCompatEditText6.perform(scrollTo(), replaceText("stevenstaph@precursor.com"))

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.emailEditText),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(closeSoftKeyboard())

        val circularProgressButton3 = onView(
            allOf(
                withId(R.id.saveChangeButton)
            )
        )
        circularProgressButton3.perform(scrollTo(), click())

        val textView = onView(
            allOf(
                withId(R.id.nameTextView),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Steven Staph")))

        val textView2 = onView(
            allOf(
                withId(R.id.phoneNumberTextView),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("22222221")))

        val textView3 = onView(
            allOf(
                withId(R.id.emailTextView),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("stevenstaph@precursor.com")))

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.menuHome),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val circularProgressButton4 = onView(
            allOf(
                withId(R.id.logOutButton),
                isDisplayed()
            )
        )
        circularProgressButton4.perform(click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText3.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("stevenstaph@precursor.com"))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText5.perform(closeSoftKeyboard())

        val circularProgressButton5 = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton5.perform(click())

        val button = onView(
            allOf(
                withId(R.id.logOutButton),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.menuProfile), withContentDescription("Profile"),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val circularProgressButton6 = onView(
            allOf(
                withId(R.id.editProfileButton)
            )
        )
        circularProgressButton6.perform(scrollTo(), click())

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.nameEditText)
            )
        )
        appCompatEditText8.perform(scrollTo(), replaceText("Steven Staff"))

        val appCompatEditText9 = onView(
            allOf(
                withId(R.id.nameEditText),
                isDisplayed()
            )
        )
        appCompatEditText9.perform(closeSoftKeyboard())

        val appCompatEditText10 = onView(
            allOf(
                withId(R.id.phoneNumberEditText)
            )
        )
        appCompatEditText10.perform(scrollTo(), replaceText("22222222"))

        val appCompatEditText11 = onView(
            allOf(
                withId(R.id.phoneNumberEditText),
                isDisplayed()
            )
        )
        appCompatEditText11.perform(closeSoftKeyboard())

        val appCompatEditText12 = onView(
            allOf(
                withId(R.id.emailEditText)
            )
        )
        appCompatEditText12.perform(scrollTo(), replaceText("stevenstaff@precursor.com"))

        val appCompatEditText13 = onView(
            allOf(
                withId(R.id.emailEditText),
                isDisplayed()
            )
        )
        appCompatEditText13.perform(closeSoftKeyboard())

        val circularProgressButton7 = onView(
            allOf(
                withId(R.id.saveChangeButton)
            )
        )
        circularProgressButton7.perform(scrollTo(), click())
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
