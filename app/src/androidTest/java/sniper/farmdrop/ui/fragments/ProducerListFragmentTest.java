package sniper.farmdrop.ui.fragments;

import android.support.v7.widget.RecyclerView;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import sniper.farmdrop.AndroidMyTestUtils;
import sniper.farmdrop.R;
import sniper.farmdrop.ui.views.ProducerListView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.hamcrest.Matchers.empty;

/**
 * Created by sniper on 17-Jan-2017.
 */

public class ProducerListFragmentTest {

    @Rule
    public FragmentTestRule<ProducerListFragment> mFragmentTestRule = new FragmentTestRule<>(ProducerListFragment.class);

    @Test
    public void testProducerListViewImplementation() throws Exception {
        //
        mFragmentTestRule.launchActivity(null);
        //We need to add some delay to give a chance to fragment to init its views and him self
        onView(withId(R.id.producers_list_rv)).perform(
                AndroidMyTestUtils.waitForWhile(200, RecyclerView.class, () -> {
                    //check does our Fragment implement the right Interface
                    assertThat(AndroidMyTestUtils.implementsInterface(mFragmentTestRule.getFragment(), ProducerListView.class), Matchers.equalTo(true));
                }));
    }

    @Test
    public void testInitialStateOfProducerListAdapter() throws Exception {
        //
        mFragmentTestRule.launchActivity(null);
        //Test the Producer list adapter in the beginning, but when some of the fragments view did load
        //We need to add some delay to give a chance to fragment to init its views and him self
        onView(withId(R.id.producers_list_rv)).perform(
                AndroidMyTestUtils.waitForWhile(200, RecyclerView.class, () -> {
                    //check adapter layout
                    assertEquals(mFragmentTestRule.getFragment().producerAdapter.getLayoutResource(), R.layout.list_item_producer_result);
                    //check the two lists in the beginning (they should be empty)
                    assertThat(mFragmentTestRule.getFragment().producerAdapter.getProducers(), empty());
                    assertThat(mFragmentTestRule.getFragment().producerAdapter.getFilterProducers(), empty());
                    //check the adapter filter mode (it should be false)
                    assertFalse("filter mode is false", mFragmentTestRule.getFragment().producerAdapter.isFilterMode());
        }));
    }
}