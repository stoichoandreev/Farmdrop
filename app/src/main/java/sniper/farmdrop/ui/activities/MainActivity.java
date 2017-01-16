package sniper.farmdrop.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.SearchView;

import sniper.farmdrop.enums.ToolbarAction;
import sniper.farmdrop.ui.fragments.ProducerListFragment;
import sniper.farmdrop.utils.AnimationUtils;
import sniper.farmdrop.utils.RegularUtils;

@VisibleForTesting
public class MainActivity extends BaseActivity implements SearchView.OnCloseListener, View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewBinding.toolbarInclude.toolbarBack.setOnClickListener((view) -> onBackPressed());

        //start the first screen
        replaceFragment(ProducerListFragment.newInstance(null), false);

        getSearchView().setOnSearchClickListener(this);
        getSearchView().setOnCloseListener(this);
    }

    @Override
    public void setToolbarAction(@NonNull ToolbarAction action, @Nullable Object... additionalParams) {
        switch (action){
            case ACTION_SHOW_TITLE:
                AnimationUtils.expand(mViewBinding.toolbarInclude.toolbarTitle);
                break;
            case ACTION_HIDE_TITLE:
                AnimationUtils.collapse(mViewBinding.toolbarInclude.toolbarTitle);
                break;
            case ACTION_SHOW_SEARCH:
                mViewBinding.toolbarInclude.producerSearchView.setVisibility(View.VISIBLE);
                break;
            case ACTION_HIDE_SEARCH:
                mViewBinding.toolbarInclude.producerSearchView.setVisibility(View.GONE);
                break;
            case ACTION_SET_TITLE:
                mViewBinding.toolbarInclude.toolbarTitle.setText(RegularUtils.parseAdditionalParams(additionalParams, 0, String.class));
                break;
            case ACTION_BACK_TO_PREVIOUS_SCREEN:
                mViewBinding.toolbarInclude.toolbarBack.setVisibility(View.VISIBLE);
                break;
            case ACTION_REMOVE_BACK_TO_PREVIOUS_SCREEN:
                mViewBinding.toolbarInclude.toolbarBack.setVisibility(View.GONE);
                break;
        }
    }
    public SearchView getSearchView(){
        return mViewBinding.toolbarInclude.producerSearchView;
    }

    @Override
    public boolean onClose() {
        setToolbarAction(ToolbarAction.ACTION_SHOW_TITLE);
        return false;
    }

    @Override
    public void onClick(View view) {
        setToolbarAction(ToolbarAction.ACTION_HIDE_TITLE);
    }
}
