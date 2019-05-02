package com.example.varuns.capstone;

import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.varuns.capstone.model.Artisan;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onData;
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
public class AddProductActivityTests {
    @Rule
    public ActivityTestRule<menu_activity> mActivityTestRule =
            new ActivityTestRule<>(menu_activity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS");

    @Test
    public void tryCancel() {
        Intents.init();

        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        onView(withId(R.id.addItem))
                .perform(click());

        onView(withId(R.id.cancelAddProduct))
                .perform(click());

        intended(hasComponent(ScrollingActivity.class.getName()));
    }

    @Test
    public void tryAddNothing() {
        Intents.init();

        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        onView(withId(R.id.addItem))
                .perform(click());

        onView(withId(R.id.submitAddProduct))
                .perform(click());

        onView(withId(R.id.addProductName))
                .check(matches(hasErrorText("Item name is required!")));
    }

    @Test
    public void tryAddNoDesc() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        onView(withId(R.id.addItem))
                .perform(click());

        Intents.init();

        String itemName = "blah";
        onView(withId(R.id.addProductName))
                .perform(typeText(itemName), closeSoftKeyboard());

        onView(withId(R.id.submitAddProduct))
                .perform(click());

        intended(hasExtra("productName", itemName));
    }

    @Test
    public void tryAddWithDesc() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        onView(withId(R.id.addItem))
                .perform(click());

        Intents.init();

        String itemName = "blah";
        onView(withId(R.id.addProductName))
                .perform(typeText(itemName), closeSoftKeyboard());

        String itemDesc = "bleh";
        onView(withId(R.id.addProductDesc))
                .perform(typeText(itemDesc), closeSoftKeyboard());

        onView(withId(R.id.submitAddProduct))
                .perform(click());

        intended(hasExtra("productName", itemName));
        intended(hasExtra("productDesc", itemDesc));
    }
}
