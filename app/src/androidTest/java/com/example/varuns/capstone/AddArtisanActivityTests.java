package com.example.varuns.capstone;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.anything;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddArtisanActivityTests {
    @Rule
    public ActivityTestRule<AddArtisanActivity> mActivityTestRule =
            new ActivityTestRule<>(AddArtisanActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS");

    @Test
    public void tryAddEmpty() {
        onView(withId(R.id.new_artisan_btn))
                .perform(click());

        onView(withId(R.id.editName))
                .check(matches(hasErrorText("Name is required!")));
    }

    @Test
    public void tryAddNoLastName() {
        onView(withId(R.id.editName))
                .perform(typeText("Blah"), closeSoftKeyboard());

        onView(withId(R.id.new_artisan_btn))
                .perform(click());

        onView(withId(R.id.editName))
                .check(matches(hasErrorText("Last name is required!")));
    }

    @Test
    public void addNewArtisanCorrect() {
        onView(withId(R.id.editName))
                .perform(typeText("Blah Blah"), closeSoftKeyboard());

        onView(withId(R.id.editBio))
                .perform(typeText("bio"), closeSoftKeyboard());

        onView(withId(R.id.editPhoneNumber))
                .perform(typeText("9999999999"), closeSoftKeyboard());

        Intents.init();
        onView(withId(R.id.new_artisan_btn))
                .perform(click());
    }
}
