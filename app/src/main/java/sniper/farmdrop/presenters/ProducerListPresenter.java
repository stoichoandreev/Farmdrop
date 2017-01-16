package sniper.farmdrop.presenters;

import android.view.View;
import android.widget.SearchView;

import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sniper.farmdrop.FarmDropApp;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.listeners.RecyclerItemClickListener;
import sniper.farmdrop.models.ProducerViewData;
import sniper.farmdrop.repos.interfaces.IProducerListRepository;
import sniper.farmdrop.ui.views.ProducerListView;

/**
 * Created by sniper on 01/13/17.
 */

public class ProducerListPresenter extends BasePresenter<ProducerListView, IProducerListRepository> implements RecyclerItemClickListener.OnItemClickListener {

    public static final int PRODUCER_PER_PAGE_LIMIT = 10;

    public ProducerListPresenter(ProducerListView view, IProducerListRepository repository){
        super(view, repository);
    }

    public void getProducerList(final int currentPage){
        Observable<ProducersListResponseParseData> observable = mRepository.requestMoreProducersFromAPI(currentPage, PRODUCER_PER_PAGE_LIMIT);
        addSubscription(observable.subscribe(new Subscriber<ProducersListResponseParseData>() {
            @Override
            public void onCompleted() {
//                mView.setProgressVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                mView.onRepositoryErrorOccurred(e);
            }

            @Override
            public void onNext(ProducersListResponseParseData producersList) {
                mView.onProducerListReady(ProducerViewData.fromRaw(producersList.producerData), (producersList.pagination != null && producersList.pagination.current != null) ? producersList.pagination.current : 0);
//                mView.onProducerListReady(ProducerViewData.fromCache(producersCacheList));
            }
        }));
    }

    @Override
    public void onItemClick(View view, int position) {
        mView.openDetailsScreen(position);
    }

    public void setupSearchView(SearchView searchView) {
        if(searchView == null) return;
        final Observable<CharSequence> searchViewQueryTextEventObservable = RxSearchView.queryTextChanges(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(charSequence -> {
                    //we can filter here some queries and stop them go to adapter
                    return true;
                });
        searchViewQueryTextEventObservable
//                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CharSequence queryTextEvent) {
                        mView.filterProducersList(queryTextEvent.toString());
                    }
                });
    }
}
