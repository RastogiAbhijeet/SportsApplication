package com.ccet.hp.sportsapplication;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.ccet.hp.sportsapplication.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<> (MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatEditText = onView (
                withId (R.id.username_field));
        appCompatEditText.perform (scrollTo (), replaceText ("co15302"), closeSoftKeyboard ());

        ViewInteraction appCompatEditText2 = onView (
                withId (R.id.password_field));
        appCompatEditText2.perform (scrollTo (), replaceText ("co15302"), closeSoftKeyboard ());

        ViewInteraction appCompatButton = onView (
                allOf (withId (R.id.loginButton), withText ("Login")));
        appCompatButton.perform (scrollTo (), click ());

        ViewInteraction appCompatImageView = onView (
                withId (R.id.profile));
        appCompatImageView.perform (scrollTo (), click ());

        ViewInteraction tableLayout = onView (
                childAtPosition (
                        withId (R.id.list),
                        1));
        tableLayout.perform (scrollTo (), click ());

        ViewInteraction tableLayout2 = onView (
                childAtPosition (
                        withId (R.id.list_events),
                        2));
        tableLayout2.perform (scrollTo (), click ());

        ViewInteraction tableLayout3 = onView (
                childAtPosition (
                        withId (R.id.list),
                        5));
        tableLayout3.perform (scrollTo (), click ());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View> () {
            @Override
            public void describeTo(Description description) {
                description.appendText ("Child at position " + position + " in parent ");
                parentMatcher.describeTo (description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent ();
                return parent instanceof ViewGroup && parentMatcher.matches (parent)
                        && view.equals (((ViewGroup) parent).getChildAt (position));
            }
        };
    }
}
