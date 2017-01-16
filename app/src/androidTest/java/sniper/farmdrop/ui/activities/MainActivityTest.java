package sniper.farmdrop.ui.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sniper.farmdrop.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.StringContains.containsString;

/**
 * Created by sniper on 16-Jan-2017.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testToolbarInitialState(){
        onView(withId(R.id.toolbar_title)).check(matches(withText(R.string.search_title)));
        onView(withId(R.id.producer_search_view)).check(matches(isDisplayed()));
    }
    @Test
    public void testFragmentContainer(){
        onView(withId(R.id.activity_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_fragment_container)).check(matches(not(isClickable())));
        onView(withId(R.id.activity_fragment_container)).check(matches(withParent(withClassName(containsString(LinearLayout.class.getCanonicalName())))));
    }
}