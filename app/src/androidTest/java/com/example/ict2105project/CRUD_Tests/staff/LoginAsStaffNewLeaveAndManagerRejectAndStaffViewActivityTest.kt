package com.example.ict2105project.CRUD_Tests.staff


import android.view.View
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.ict2105project.LoginActivity
import com.example.ict2105project.R
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginAsStaffNewLeaveAndManagerRejectAndStaffViewActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginAsStaffNewLeaveAndManagerRejectAndStaffViewActivityTest() {
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
                withId(R.id.menuLeave),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val circularProgressButton2 = onView(
            allOf(
                withId(R.id.applyLeaveButton),
                isDisplayed()
            )
        )
        circularProgressButton2.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.startDateButton),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withClassName(`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Next month")
            )
        )
        appCompatImageButton2.perform(scrollTo(), click())

        val appCompatImageButton3 = onView(
            allOf(
                withClassName(`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Next month")
            )
        )
        appCompatImageButton3.perform(scrollTo(), click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK")
            )
        )
        materialButton.perform(scrollTo(), click())

        onView(withId(R.id.startDateTextView)).perform(setTextInTextView("30-5-2022"))

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.endDateButton),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())

        val appCompatImageButton5 = onView(
            allOf(
                withClassName(`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Next month")
            )
        )
        appCompatImageButton5.perform(scrollTo(), click())

        val appCompatImageButton6 = onView(
            allOf(
                withClassName(`is`("androidx.appcompat.widget.AppCompatImageButton")),
                withContentDescription("Previous month")
            )
        )
        appCompatImageButton6.perform(scrollTo(), click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("OK")
            )
        )
        materialButton2.perform(scrollTo(), click())

        onView(withId(R.id.endDateTextView)).perform(setTextInTextView("31-5-2022"))

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.leaveReasonTextInput),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("Birthday celebration"), closeSoftKeyboard())

        val circularProgressButton3 = onView(
            allOf(
                withId(R.id.leave_confirmation_button),
                isDisplayed()
            )
        )
        circularProgressButton3.perform(click())

        val materialButton3 = onView(
            allOf(
                withId(android.R.id.button1)
            )
        )
        materialButton3.perform(scrollTo(), click())

        val materialButton4 = onView(
            allOf(
                withId(R.id.leave_confirm2_button),
                isDisplayed()
            )
        )
        materialButton4.perform(click())

        val circularProgressButton4 = onView(
            allOf(
                withId(R.id.viewLeaveButton),
                isDisplayed()
            )
        )
        circularProgressButton4.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.leaveDateFromEmployeeTextView),
                isDisplayed()
            )
        )
        textView.check(matches(withText("30-5-2022")))

        val textView2 = onView(
            allOf(
                withId(R.id.leaveDateToEmployeeTextView),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("31-5-2022")))

        val textView3 = onView(
            allOf(
                withId(R.id.ReasonEmployeeViewTextView),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Birthday celebration")))

        val textView4 = onView(
            allOf(
                withId(R.id.leaveStatusEmployeeTextView),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("PENDING")))

        val button = onView(
            allOf(
                withId(R.id.employeeLeaveViewRefreshPageButton),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        pressBack()

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.menuHome),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val circularProgressButton5 = onView(
            allOf(
                withId(R.id.logOutButton),
                isDisplayed()
            )
        )
        circularProgressButton5.perform(click())

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("mikemanager@precursor.com"))

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText5.perform(closeSoftKeyboard())

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("mikemanager"))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText7.perform(closeSoftKeyboard())

        val circularProgressButton6 = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton6.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.menuLeave),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val circularProgressButton7 = onView(
            allOf(
                withId(R.id.approveLeaveButton),
                isDisplayed()
            )
        )
        circularProgressButton7.perform(click())

        val appCompatImageButton7 = onView(
            allOf(
                withId(R.id.rejectLeaveManagerImageButton),
                isDisplayed()
            )
        )
        appCompatImageButton7.perform(click())

        val circularProgressButton8 = onView(
            allOf(
                withId(R.id.managerClearLeaveSelectionButton),
                isDisplayed()
            )
        )
        circularProgressButton8.perform(click())

        val materialButton5 = onView(
            allOf(
                withId(android.R.id.button1),
            )
        )
        materialButton5.perform(scrollTo(), click())

        val appCompatImageButton8 = onView(
            allOf(
                withId(R.id.rejectLeaveManagerImageButton),
                isDisplayed()
            )
        )
        appCompatImageButton8.perform(click())

        val circularProgressButton9 = onView(
            allOf(
                withId(R.id.managerApproveLeaveButton),
                isDisplayed()
            )
        )
        circularProgressButton9.perform(click())

        val materialButton6 = onView(
            allOf(
                withId(android.R.id.button1),
            )
        )
        materialButton6.perform(scrollTo(), click())

        val bottomNavigationItemView4 = onView(
            allOf(
                withId(R.id.menuHome), withContentDescription("Home"),
                isDisplayed()
            )
        )
        bottomNavigationItemView4.perform(click())

        val circularProgressButton10 = onView(
            allOf(
                withId(R.id.logOutButton),
                isDisplayed()
            )
        )
        circularProgressButton10.perform(click())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("stevenstaff@precursor.com"))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText10.perform(replaceText("stevenstaff"))

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText11.perform(closeSoftKeyboard())

        val circularProgressButton11 = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton11.perform(click())

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.menuLeave), withContentDescription("Leave"),
                isDisplayed()
            )
        )
        bottomNavigationItemView5.perform(click())

        val circularProgressButton12 = onView(
            allOf(
                withId(R.id.viewLeaveButton),
                isDisplayed()
            )
        )
        circularProgressButton12.perform(click())

        val textView5 = onView(
            allOf(
                withId(R.id.leaveStatusEmployeeTextView),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("REJECTED")))
    }
    private fun setTextInTextView(value: String): ViewAction {
        return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                        return CoreMatchers.allOf(
                                ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(
                                        TextView::class.java))
                }

                override fun perform(uiController: UiController, view: View) {
                        (view as TextView).text = value
                }

                override fun getDescription(): String {
                        return "replace text"
                }
        }
    }
}
