package sniper.farmdrop.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.List;
import sniper.farmdrop.R;
import sniper.farmdrop.adapter.EndlessRecyclerOnScrollListener;
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
    private EndlessRecyclerOnScrollListener endlessListener;
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
        producerAdapter = new ProducerAdapter();
    }

    @Override //No implementation need at the moment
    protected void initDataInstances(Bundle savedInstanceState) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_producer_list, container, false);

        if(endlessListener == null) {
            endlessListener = new EndlessRecyclerOnScrollListener(new LinearLayoutManager(mActivity)) {
                @Override
                public void onLoadMore(int currentPage) {
                    Log.d("Producer List", "Load more Producers for page = " + Integer.toString(currentPage));
                    mPresenter.getProducerList(currentPage);
                }
            };
            mPresenter.getProducerList(endlessListener.getCurrentPage());
        }else {
            endlessListener.setLayoutManager(new LinearLayoutManager(mActivity));
            endlessListener.onScrolled(fragmentBinding.producersListRv, 0, 0);
        }
        fragmentBinding.producersListRv.setLayoutManager(endlessListener.getLinearLayoutManager());
        fragmentBinding.producersListRv.setAdapter(producerAdapter);
        fragmentBinding.producersListRv.addOnItemTouchListener(new RecyclerItemClickListener(mActivity, mPresenter));
        fragmentBinding.producersListRv.addOnScrollListener(endlessListener);

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
    public void onProducerListReady(List<ProducerViewData> producersListResult, int page) {
        //when the Repository response with data about some page, we need to set this page to the endless listener, so both repo and the listener will be in sync for the next request
        final boolean newItems = endlessListener.getCurrentPage() == page;
        endlessListener.setCurrentPage(page);
        endlessListener.setLoading(false);
        //if the coming data is new then insert into the list
        if(newItems){
            producerAdapter.setMoreProducers(producersListResult);
        }
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
    public void filterProducersList(String queryText) {
        producerAdapter.filter(queryText, this, endlessListener.getFirstPosition());
    }

    @Override
    public void scrollListToPosition(int position) {
        endlessListener.getLinearLayoutManager().scrollToPosition(position);
    }

    @Override
    public void onRepositoryErrorOccurred(Throwable error) {
        if(getView() != null) {
            Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        }
        endlessListener.setLoading(false);
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
