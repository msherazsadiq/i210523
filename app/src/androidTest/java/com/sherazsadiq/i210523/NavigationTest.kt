package com.sherazsadiq.i210523

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.sherazsadiq.i210523.login
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationTest {

    @Test
    fun testLoginToHomeActivity() {
        // Launch the MainActivity
        ActivityScenario.launch(login::class.java)

        // Perform a click on the button
        onView(withId(R.id.loginbutton)).perform(click())

        // Check if SecondActivity is displayed
        onView(withId(R.id.notificationButton)).check(matches(isDisplayed()))
    }

    @Test
    fun testLoginToForgetPassword() {
        // Launch the MainActivity
        ActivityScenario.launch(login::class.java)

        // Perform a click on the button
        onView(withId(R.id.forgotPasswordButton)).perform(click())

        // Check if SecondActivity is displayed
        onView(withId(R.id.emailSendButton)).check(matches(isDisplayed()))
    }

}