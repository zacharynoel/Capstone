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

import static android.provider.Settings.Global.getString;
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
public class MenuActivityTests {

    @Rule
    public ActivityTestRule<menu_activity> mActivityTestRule =
            new ActivityTestRule<>(menu_activity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS");

    @Test
    public void addArtisanButtonTest() {
        Intents.init();

        onView(withId(R.id.add_artisan_btn))
                .perform(click());

        intended(hasComponent(AddArtisanActivity.class.getName()));
    }

    @Test
    public void selectArtisanDetailView() {
        Intents.init();
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        intended(hasComponent(ScrollingActivity.class.getName()));

        intended(hasExtra("artisanId", artisans.get(0).getArtisanId()));
    }
}
