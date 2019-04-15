package com.example.varuns.capstone;

import android.content.Intent;
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
public class ArtisanDetailActivityTests {
    @Rule
    public ActivityTestRule<menu_activity> mActivityTestRule =
            new ActivityTestRule<>(menu_activity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS");

    @Test
    public void editArtisanProducts() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        Intents.init();

        onData(anything()).inAdapterView(withId(R.id.artisanProductList))
                .atPosition(0).perform(click());

        //TODO - Owen, idk what's going on with your buttons
        //onView(withId(R.id.button3))
          //      .perform(click());

        //intended(hasComponent(EditProduct.class.getName()));
    }

    @Test
    public void editArtisan() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        Intents.init();

        onView(withId(R.id.editArtisan))
                .perform(click());

        intended(hasComponent(EditArtisan.class.getName()));

        intended(hasExtra("artisanId", artisans.get(0).getArtisanId()));
    }


    @Test
    public void goToArtisanReport() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        Intents.init();

        onView(withId(R.id.viewReport))
                .perform(click());

        intended(hasComponent(ReportsActivity.class.getName()));

        intended(hasExtra("artisanId", artisans.get(0).getArtisanId()));
    }

    @Test
    public void addItemToArtisan() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        Intents.init();

        onView(withId(R.id.addItem))
                .perform(click());

        intended(hasComponent(AddProduct.class.getName()));

        intended(hasExtra("artisanId", artisans.get(0).getArtisanId()));
    }

    @Test
    public void openArtisanMessages() {
        List<Artisan> artisans = mActivityTestRule.getActivity()
                .getArtisanAdapterGlobal().getArtisans();

        assertTrue(artisans.size() > 0);

        onData(anything()).inAdapterView(withId(R.id.artisanList))
                .atPosition(0).perform(click());

        Intents.init();

        onView(withId(R.id.fab))
                .perform(click());

        intended(hasComponent(Send_message.class.getName()));

        intended(hasExtra("phoneNo", artisans.get(0).getPhoneNo()));
    }
}
