package com.example.ict2105project.CRUD_Tests.staff


import android.view.View
import android.view.ViewGroup
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
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginAsStaffApplyClaimsAndManagerApprovesAndStaffViewsClaimsActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Test
    fun loginAsStaffApplyClaimsAndManagerApprovesAndStaffViewsActivityTest() {
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
                withId(R.id.menuClaims), withContentDescription("Claims"),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val circularProgressButton2 = onView(
            allOf(
                withId(R.id.employeeCreateNewClaimButton),
                isDisplayed()
            )
        )
        circularProgressButton2.perform(click())

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.employeeClaimsDateButton),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val materialButton = onView(
            allOf(
                withId(android.R.id.button1), withText("OK"),
            )
        )
        materialButton.perform(scrollTo(), click())

        val textInputEditText3 = onView(
            allOf(
                withId(R.id.employeeClaimsAmountEditText),
                isDisplayed()
            )
        )
        textInputEditText3.perform(replaceText("2000"), closeSoftKeyboard())

        onView(withId(R.id.employeeClaimsDateInputTextView)).perform(setTextInTextView("1-3-2022"))

        val textInputEditText4 = onView(
            allOf(
                withId(R.id.employeeClaimsItemImageLinkEditText),
                isDisplayed()
            )
        )
        textInputEditText4.perform(replaceText("https://picsum.photos/200"), closeSoftKeyboard())

        val textInputEditText5 = onView(
            allOf(
                withId(R.id.employeeClaimsReasonEditText),
                isDisplayed()
            )
        )
        textInputEditText5.perform(replaceText("testing"), closeSoftKeyboard())

        val circularProgressButton3 = onView(
            allOf(
                withId(R.id.employeeClaimsSubmitButton), withText("Submit"),
                isDisplayed()
            )
        )
        circularProgressButton3.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button1), withText("Confirm")
            )
        )
        materialButton2.perform(scrollTo(), click())

        val materialButton3 = onView(
            allOf(
                withId(R.id.claimsAlertConfirmButton),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val circularProgressButton4 = onView(
            allOf(
                withId(R.id.employeeViewPendingClaimButton), withText("Pending Claims"),
                isDisplayed()
            )
        )
        circularProgressButton4.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.employeeClaimsItemTypeTextView), withText("Advertising"),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Advertising")))

        val textView2 = onView(
            allOf(
                withId(R.id.employeeClaimsItemReasonTextView),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("testing")))

        val textView3 = onView(
            allOf(
                withId(R.id.employeeClaimsItemAmountTextView),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("$2000.00")))

        val textView4 = onView(
            allOf(
                withId(R.id.employeeClaimItemStatusTextView),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("PENDING")))

        val imageButton = onView(
            allOf(
                withId(R.id.employeeClaimsItemImageLinkButton),
                withParent(withParent(withId(R.id.employeeClaimsItemCardView))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val textView5 = onView(
            allOf(
                withId(R.id.employeeClaimsItemDateTextView),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("1-3-2022")))

        val button = onView(
            allOf(
                withId(R.id.employeeClaimsPendingRefreshPageButton),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        pressBack()

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.menuHome), withContentDescription("Home"),
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

        val textInputEditText6 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText6.perform(replaceText("mikemanager@precursor.com"))

        val textInputEditText7 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText7.perform(closeSoftKeyboard())

        val textInputEditText8 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText8.perform(replaceText("mikemanager"))

        val textInputEditText9 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText9.perform(closeSoftKeyboard())

        val circularProgressButton6 = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton6.perform(click())

        val bottomNavigationItemView3 = onView(
            allOf(
                withId(R.id.menuClaims), withContentDescription("Claims"),
                isDisplayed()
            )
        )
        bottomNavigationItemView3.perform(click())

        val circularProgressButton7 = onView(
            allOf(
                withId(R.id.managerManageAllClaimButton),
                isDisplayed()
            )
        )
        circularProgressButton7.perform(click())

        val appCompatImageButton2 = onView(
            allOf(
                withId(R.id.managerClaimsItemApproveButton),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())

        val circularProgressButton8 = onView(
            allOf(
                withId(R.id.managerManageClaimsClearSelectionButton),
                isDisplayed()
            )
        )
        circularProgressButton8.perform(click())

        val materialButton4 = onView(
            allOf(
                withId(android.R.id.button1), withText("Confirm"),
            )
        )
        materialButton4.perform(scrollTo(), click())

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.managerClaimsItemApproveButton),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())

        val circularProgressButton9 = onView(
            allOf(
                withId(R.id.managerManageClaimsSubmitApproveRejectButton),
                isDisplayed()
            )
        )
        circularProgressButton9.perform(click())

        val materialButton5 = onView(
            allOf(
                withId(android.R.id.button1), withText("Confirm")
            )
        )
        materialButton5.perform(scrollTo(), click())

        val materialButton6 = onView(
            allOf(
                withId(R.id.claimsManageAlertConfirmButton),
                isDisplayed()
            )
        )
        materialButton6.perform(click())

        pressBack()

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

        val textInputEditText10 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText10.perform(click())

        val textInputEditText11 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText11.perform(replaceText("stevenstaff@precursor.com"))

        val textInputEditText12 = onView(
            allOf(
                withId(R.id.LoginEmailEditText),
                isDisplayed()
            )
        )
        textInputEditText12.perform(closeSoftKeyboard())

        val textInputEditText13 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText13.perform(replaceText("stevenstaff"))

        val textInputEditText17 = onView(
            allOf(
                withId(R.id.LoginPasswordText),
                isDisplayed()
            )
        )
        textInputEditText17.perform(closeSoftKeyboard())

        val circularProgressButton11 = onView(
            allOf(
                withId(R.id.LoginButton),
                isDisplayed()
            )
        )
        circularProgressButton11.perform(click())

        val bottomNavigationItemView5 = onView(
            allOf(
                withId(R.id.menuClaims), withContentDescription("Claims"),
                isDisplayed()
            )
        )
        bottomNavigationItemView5.perform(click())

        val circularProgressButton12 = onView(
            allOf(
                withId(R.id.employeeViewApprovedRejectedClaimButton),
                isDisplayed()
            )
        )
        circularProgressButton12.perform(click())

        val textView6 = onView(
            allOf(
                withId(R.id.employeeClaimItemStatusTextView),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("APPROVED")))

        val textView7 = onView(
            allOf(
                withId(R.id.employeeRejectedClaimsEmptyTextView),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("No Items To Display")))
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
