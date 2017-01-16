package sniper.farmdrop.ui.fragments;

import org.junit.Rule;
import org.junit.Test;

import sniper.farmdrop.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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

    @Test
    public void fragment_can_be_instantiated() {

        // Launch the activity to make the fragment visible
        mFragmentTestRule.launchActivity(null);

        // Then use Espresso to test the Fragment
        onView(withId(R.id.details_container)).check(matches(isDisplayed()));
    }
}