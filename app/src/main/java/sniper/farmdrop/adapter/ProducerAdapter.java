package sniper.farmdrop.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sniper.farmdrop.BR;
import sniper.farmdrop.R;
import sniper.farmdrop.models.ProducerViewData;

/**
 * Created by sniper on 01/13/17.
 */

public class ProducerAdapter extends RecyclerView.Adapter<ProducerAdapter.BindingMovieViewHolder> {

    private List<ProducerViewData> mProducers = new ArrayList<>();

    public ProducerAdapter(@NonNull List<ProducerViewData> movies) {
        super();
        this.mProducers.addAll(movies);
    }

    @Override
    public BindingMovieViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_producer_result, parent, false);
        return new BindingMovieViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BindingMovieViewHolder holder, int position) {
        final ProducerViewData producerViewData = mProducers.get(position);
        holder.getBinding().setVariable(BR.producer, producerViewData);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProducers.size();
    }
    public void setMoreProducers(List<ProducerViewData> producers){
        this.mProducers.clear();
        if(producers != null) this.mProducers.addAll(producers);
        notifyDataSetChanged();
    }
    public static class BindingMovieViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding viewHolderBinding;

        public BindingMovieViewHolder(View v) {
            super(v);
            viewHolderBinding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return viewHolderBinding;
        }
    }
    public ProducerViewData getProducer(int position){
        return (position >= 0 && position < mProducers.size()) ? mProducers.get(position) : null;
    }
}
