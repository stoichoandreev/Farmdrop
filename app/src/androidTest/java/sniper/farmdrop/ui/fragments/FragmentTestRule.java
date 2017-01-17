package sniper.farmdrop.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;

import org.junit.Assert;

import sniper.farmdrop.R;
import sniper.farmdrop.ui.activities.MainActivity;

/**
 * Created by sniper on 01/14/17.
 * This class will give us chance to test our Fragments in isolation.
 */

public class FragmentTestRule <F extends Fragment> extends ActivityTestRule<MainActivity> {

    private final Class<F> mFragmentClass;
    private F mFragment;
    private Bundle fragmentArguments;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(MainActivity.class, true, false);
        mFragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(() -> {
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                mFragment = mFragmentClass.newInstance();
                mFragment.setArguments(fragmentArguments);
                transaction.replace(R.id.activity_fragment_container, mFragment);
                transaction.commit();
            } catch (Exception e) {
                Assert.fail(String.format("%s: Could not insert %s into Test Activity: %s",
                        getClass().getSimpleName(),
                        mFragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }
    public F getFragment(){
        return mFragment;
    }
    public void setFragmentArguments(Bundle args){
        this.fragmentArguments = args;
    }
}
