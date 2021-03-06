package sniper.farmdrop.ui.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.LinearLayout;
import android.widget.SearchView;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sniper.farmdrop.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
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
        //test toolbar default title
        onView(withId(R.id.toolbar_title)).check(matches(withText(R.string.search_title)));
        //at the beginning toolbar back arrow should be not visible
        onView(withId(R.id.toolbar_back)).check(matches(not(isDisplayed())));
    }
    @Test
    public void testFragmentContainer(){
        onView(withId(R.id.activity_fragment_container))
                .check(matches(isDisplayed()))
                .check(matches(not(isClickable())))
                .check(matches(withParent(withClassName(containsString(LinearLayout.class.getCanonicalName())))));
    }
    @Test
    public void testSearchView(){
        //test search view
        onView(withId(R.id.producer_search_view)).check(matches(isDisplayed()));
        //check does search view is not null
        assertThat(((MainActivity)mActivityRule.getActivity()).getSearchView(), notNullValue());
        //check does search view is SearchView.class object
        assertThat(((MainActivity)mActivityRule.getActivity()).getSearchView(), withClassName(containsString(SearchView.class.getCanonicalName())));
        //check does search view query is empty at the beginning
        assertThat(((MainActivity)mActivityRule.getActivity()).getSearchView().getQuery().toString(), Matchers.equalTo(""));
        //check query hint (should not exist at all)
        assertThat(((MainActivity)mActivityRule.getActivity()).getSearchView().getQueryHint(), Matchers.equalTo(null));
    }
    @Test
    public void testSearchViewClick(){
        //perform search bar click
        onView(withId(R.id.producer_search_view)).perform(click());
        //then test the screen title visibility (should be hidden)
        onView(withId(R.id.toolbar_title)).check(matches(not(isDisplayed())));

    }
}