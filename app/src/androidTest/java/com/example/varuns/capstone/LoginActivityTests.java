package com.example.varuns.capstone;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.provider.Settings.Global.getString;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginActivityTests {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS");

    @Test
    public void emptyEmailTest() {
        onView(withId(R.id.email_sign_in_button))
                .perform(click());

        onView(withId(R.id.email))
                .check(matches(hasErrorText("This field is required")));
    }

    @Test
    public void badEmailTest() {
        onView(withId(R.id.email))
                .perform(typeText("blah"), closeSoftKeyboard());

        onView(withId(R.id.email_sign_in_button))
                .perform(click());

        onView(withId(R.id.email))
                .check(matches(hasErrorText("This email address is invalid")));
    }

    @Test
    public void badPasswordTest() {
        onView(withId(R.id.email))
                .perform(typeText("blah@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText("a"));

        onView(withId(R.id.email_sign_in_button))
                .perform(click());

        onView(withId(R.id.password))
                .check(matches(hasErrorText("This password is too short")));
    }
}
