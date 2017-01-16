package sniper.farmdrop.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sniper.farmdrop.R;
import sniper.farmdrop.adapter.ProducerAdapter;
import sniper.farmdrop.databinding.FragmentProducerListBinding;
import sniper.farmdrop.enums.FragmentAction;
import sniper.farmdrop.enums.ToolbarAction;
import sniper.farmdrop.listeners.RecyclerItemClickListener;
import sniper.farmdrop.models.ProducerViewData;
import sniper.farmdrop.presenters.ProducerListPresenter;
import sniper.farmdrop.repos.ProducerListRepository;
import sniper.farmdrop.ui.activities.MainActivity;
import sniper.farmdrop.ui.views.ProducerListView;

/**
 * Created by sniper on 01/13/17.
 * When user scroll the list and scroll goes to the end we are requesting more data
 */
public class ProducerListFragment extends BaseFragment<ProducerListPresenter> implements ProducerListView {

    private FragmentProducerListBinding fragmentBinding;
    private ProducerAdapter producerAdapter;
    private List<ProducerViewData> producersList;
    private boolean isResuming;

    @UiThread
    public static ProducerListFragment newInstance(@Nullable Bundle args) {
        ProducerListFragment fragment = new ProducerListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ProducerListPresenter(this, new ProducerListRepository());
    }

    @Override //No implementation need for now
    protected void initDataInstances(Bundle savedInstanceState) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_producer_list, container, false);
        fragmentBinding.producersListRv.setLayoutManager(new LinearLayoutManager(mActivity));
        fragmentBinding.producersListRv.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, mPresenter));

        producerAdapter = new ProducerAdapter(producersList != null ? producersList : new ArrayList<>(0));
        fragmentBinding.producersListRv.setAdapter(producerAdapter);

        mPresenter.getProducerList();
        setupSearch();

        return fragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.setToolbarAction(ToolbarAction.ACTION_SET_TITLE, getStringResourceSafety(R.string.search_title));
        mActivity.setToolbarAction(ToolbarAction.ACTION_REMOVE_BACK_TO_PREVIOUS_SCREEN);
        mActivity.setToolbarAction(ToolbarAction.ACTION_SHOW_SEARCH);
    }

    @Override
    public void onPause() {
        super.onPause();
        isResuming = true;
    }

    @Override
    public void notifyFragmentAboutAction(FragmentAction fragmentAction) {
        switch (fragmentAction) {
            case ACTION_CONNECTIVITY_CHANGE:
                //Can Implement something here if connection change (present diff look and feel)
                break;
        }
    }

    @Override
    public void onProducerListReady(List<ProducerViewData> producersListResult) {
        producersList = producersListResult;
        producerAdapter.setMoreProducers(producersList);
    }

    @Override
    public void openDetailsScreen(int position) {
        final Bundle bundle = new Bundle();
        bundle.putSerializable(DetailsProducerFragment.PRODUCER_DATA, producerAdapter.getProducer(position));
        mActivity.replaceFragment(DetailsProducerFragment.newInstance(bundle), true);
    }

    @Override
    public void setIsResuming(boolean isResume) {
        this.isResuming = isResume;
    }

    @Override
    public boolean isResuming() {
        return isResuming;
    }

    @Override
    public void onRepositoryErrorOccurred(Throwable error) {
        if(getView() != null) {
            Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setProgressVisibility(int visibility) {
        mActivity.runOnUiThread(() -> {
            fragmentBinding.searchProgressBar.setVisibility(visibility);//simple progress bar can't be stop, but we can hide it
        });
    }

    private void setupSearch() {
        if(mActivity instanceof MainActivity){
            mPresenter.setupSearchView(((MainActivity)mActivity).getSearchView());
        }
    }
}
