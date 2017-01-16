package sniper.farmdrop.ui.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import sniper.farmdrop.R;
import sniper.farmdrop.databinding.FragmentProducerDetailsBinding;
import sniper.farmdrop.enums.FragmentAction;
import sniper.farmdrop.enums.ToolbarAction;
import sniper.farmdrop.models.ProducerDetailsViewData;
import sniper.farmdrop.models.ProducerViewData;
import sniper.farmdrop.presenters.DetailsProducerPresenter;
import sniper.farmdrop.repos.DetailsProducerRepository;
import sniper.farmdrop.ui.views.DetailsProducerView;

/**
 * Created by sniper on 01/13/17.
 *
 */
public class DetailsProducerFragment extends BaseFragment<DetailsProducerPresenter> implements DetailsProducerView {

    public static final String PRODUCER_DATA = "producer_data";

    private FragmentProducerDetailsBinding fragmentBinding;
    private ProducerDetailsViewData producerDetailsData;
    private boolean doesDataLoadErrorHappens;

    @UiThread
    public static DetailsProducerFragment newInstance(@Nullable Bundle args) {
        DetailsProducerFragment fragment = new DetailsProducerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new DetailsProducerPresenter(this, new DetailsProducerRepository());
        final ProducerViewData producerBaseData = getArguments() != null ? (ProducerViewData) getArguments().getSerializable(PRODUCER_DATA) : null;
        //get some data from producer base data and create not full data object about producer details. When the producer details data comes from local cache or API the view will be updated.
        producerDetailsData = ProducerDetailsViewData.fromBasicData(producerBaseData);
    }

    @Override
    protected void initDataInstances(Bundle savedInstanceState) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_producer_details, container, false);
        //Views are ready give them some init data, when the presenter return all producer details the View will be updated
        onProducerDetailsReady(producerDetailsData);
        //call presenter to get fresh data
        mPresenter.getProducerDetailsById();

        return fragmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.setToolbarAction(ToolbarAction.ACTION_SET_TITLE, getStringResourceSafety(R.string.details_title));
        mActivity.setToolbarAction(ToolbarAction.ACTION_BACK_TO_PREVIOUS_SCREEN);
        mActivity.setToolbarAction(ToolbarAction.ACTION_HIDE_SEARCH);
    }

    @Override
    public void notifyFragmentAboutAction(FragmentAction fragmentAction) {
        switch (fragmentAction) {
            case ACTION_CONNECTIVITY_CHANGE:
                if(doesDataLoadErrorHappens){
                    doesDataLoadErrorHappens = false;
                    mPresenter.getProducerDetailsById();
                }
                break;
        }
    }

    @Override
    public void onProducerDetailsReady(ProducerDetailsViewData producerDetails) {
        if(producerDetails != null) {
            fragmentBinding.setDetailsData(producerDetails);
        }
    }

    @Override
    public long getProducerId() {
        return producerDetailsData.getProducerId();
    }

    @Override
    public void onRepositoryErrorOccurred(Throwable error) {
        Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG).show();
        fragmentBinding.setDetailsData(new ProducerDetailsViewData.Builder().build());//this will init all views with default values (In case of request error and no local cache), otherwise they will stay empty
        doesDataLoadErrorHappens = true;
    }

    @Override
    public void setProgressVisibility(int visibility) {
        fragmentBinding.loadingView.setVisibility(visibility);
        switch (visibility){
            case View.VISIBLE:
                fragmentBinding.detailsContainer.setVisibility(View.GONE);
                break;
            case View.GONE:
            case View.INVISIBLE:
                fragmentBinding.detailsContainer.setVisibility(View.VISIBLE);
                break;
        }
    }
}
