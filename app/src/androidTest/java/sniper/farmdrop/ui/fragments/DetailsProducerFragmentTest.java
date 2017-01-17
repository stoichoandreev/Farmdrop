package sniper.farmdrop.ui.fragments;

import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import sniper.farmdrop.R;
import sniper.farmdrop.models.ProducerViewData;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Created by sniper on 01/16/17.
 * Short Example, how we can test our Fragments (isolated)
 * NOTE: Because we have Android native view binding this kind of tests have a problem to run
 *      Workaround for this problem is https://code.google.com/p/android/issues/detail?id=182715
 *      The Android Gradle plugin is creating DataBindingExportBuildInfoTasks for the instrumentation
 *      APK that generates from the app APKs layouts. This creates duplicate classes in the app and
 *      instrumentation APK which leads to
 *      java.lang.IllegalAccessError: Class ref in pre-verified class resolved to unexpected implementation
 *      on older devices.
 *      The workaround is to get the DataBindingExportBuildInfoTasks tasks for the instrumentation APK
 *      and delete the files right after it creates them.
 *
        tasks.withType(com.android.build.gradle.internal.tasks.databinding.DataBindingExportBuildInfoTask) { task ->
            if (task.name.endsWith("AndroidTest")) {
                task.finalizedBy(tasks.create("${task.name}Workaround") << {
                    task.output.deleteDir()
                })
            }
        }
 */
public class DetailsProducerFragmentTest {
    @Rule
    public FragmentTestRule<DetailsProducerFragment> mFragmentTestRule = new FragmentTestRule<>(DetailsProducerFragment.class);

    @Before
    public void setUp() throws Exception {
        final ProducerViewData mockProducerData = new ProducerViewData.Builder()
                .producerId(170)
                .name("My Producer Name")
                .description("I am part of the farm drop network")
                .build();
        Bundle args = new Bundle();
        args.putSerializable(DetailsProducerFragment.PRODUCER_DATA, mockProducerData);
        mFragmentTestRule.setFragmentArguments(args);
    }

    @Test
    public void testFragmentStartPointWhereLoadingViewNeedToBeVisible() {
        // Launch the activity to make the fragment visible. We can pass some Intent if we need.
        mFragmentTestRule.launchActivity(null);
        // Then use Espresso to test the Fragment
        onView(withId(R.id.details_container)).check(matches(isDisplayed()));
        onView(withId(R.id.loading_view)).check(matches(isDisplayed()));
    }
    @Test
    public void testFragmentInitialViewsIfLoadingViewDisappears() {
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.loading_view)).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "loading view";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(View.GONE);
            }
        }).check(matches(not(isDisplayed())));

        onView(withId(R.id.details_container)).check(matches(isDisplayed()));
    }
}