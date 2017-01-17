package sniper.farmdrop.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import sniper.farmdrop.BR;
import sniper.farmdrop.R;
import sniper.farmdrop.models.ProducerViewData;
import sniper.farmdrop.ui.views.ProducerListView;

/**
 * Created by sniper on 01/13/17.
 */

public class ProducerAdapter extends RecyclerView.Adapter<ProducerAdapter.BindingMovieViewHolder> {

    private List<ProducerViewData> mProducers = new ArrayList<>();
    private List<ProducerViewData> mFilterProducers = new ArrayList<>();
    private boolean isFilterMode;
    private CompositeSubscription mCompositeSubscription;
    private int lastNonFilterStartingPosition;//save the last state of the adapter before filtering start
    private int layoutResource;

    public ProducerAdapter(@LayoutRes int layout) {
        this.mCompositeSubscription = new CompositeSubscription();
        this.layoutResource = layout;
    }

    @Override
    public BindingMovieViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getLayoutResource(), parent, false);
        return new BindingMovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BindingMovieViewHolder holder, int position) {
        final ProducerViewData producerViewData = getProducer(position);
        holder.getBinding().setVariable(BR.producer, producerViewData);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return isFilterMode ? mFilterProducers.size() : mProducers.size();
    }
    public void setMoreProducers(List<ProducerViewData> producers){
        //Only if we have something to insert
        if(producers != null && producers.size() > 0) {
            final int insertStart = mProducers.size();
            final int insertSize = producers.size();
            this.mProducers.addAll(producers);
            notifyItemRangeInserted(insertStart, insertSize);
        }
    }

    static class BindingMovieViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding viewHolderBinding;

        BindingMovieViewHolder(View v) {
            super(v);
            viewHolderBinding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return viewHolderBinding;
        }
    }
    public ProducerViewData getProducer(int position){
        return (position >= 0 && position < getItemCount()) ? (isFilterMode? mFilterProducers.get(position) : mProducers.get(position) ) : null;
    }
    public void filter(String filterText, ProducerListView view, int firstVisiblePosition){
        mFilterProducers.clear();
        if(!isFilterMode){
            lastNonFilterStartingPosition = firstVisiblePosition;
        }
        isFilterMode = (filterText != null && filterText.trim().length() > 0);
        //remove filter mode and show all items if query is empty (when filter search finish)
        if(!isFilterMode){
            notifyDataSetChanged();
            view.scrollListToPosition(lastNonFilterStartingPosition);
            clearAllSubscriptions();
            return;
        }

        Observable.from(mProducers)
                .subscribeOn(Schedulers.io())
                .filter(producerViewData -> producerViewData.getName() != null && producerViewData.getName().toLowerCase().startsWith(filterText.toLowerCase()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ProducerViewData>() {
                    @Override
                    public void onCompleted() {
                        notifyDataSetChanged();//When filter finish notify adapter
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("Filter","error");
                    }
                    @Override
                    public void onNext(ProducerViewData producerViewData) {
                        mFilterProducers.add(producerViewData);
                    }
                });
    }

    /**
     * Use this method to clear all Subscriptions after filter search has been finished
     */
    private void clearAllSubscriptions(){
        if(mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = new CompositeSubscription();
        }
    }
    public int getLayoutResource() {
        return layoutResource;
    }

    public List<ProducerViewData> getProducers() {
        return mProducers;
    }

    public List<ProducerViewData> getFilterProducers() {
        return mFilterProducers;
    }

    public boolean isFilterMode() {
        return isFilterMode;
    }
}
