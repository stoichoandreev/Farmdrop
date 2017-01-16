package sniper.farmdrop.presenters;

import android.view.View;
import android.widget.SearchView;

import com.jakewharton.rxbinding.widget.RxSearchView;
import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.listeners.RecyclerItemClickListener;
import sniper.farmdrop.models.ProducerViewData;
import sniper.farmdrop.repos.interfaces.IProducerListRepository;
import sniper.farmdrop.ui.views.ProducerListView;

/**
 * Created by sniper on 01/13/17.
 */

public class ProducerListPresenter extends BasePresenter<ProducerListView, IProducerListRepository> implements RecyclerItemClickListener.OnItemClickListener {

    private int producersPage = 0;
    public static final int PRODUCER_PER_PAGE_LIMIT = 10;

    public ProducerListPresenter(ProducerListView view, IProducerListRepository repository){
        super(view, repository);
    }

    public void getProducerList(){

        producersPage++;

        Observable<ProducersListResponseParseData> observable = mRepository.requestMoreProducersFromAPI(producersPage, PRODUCER_PER_PAGE_LIMIT);
        addSubscription(observable.subscribe(new Subscriber<ProducersListResponseParseData>() {
            @Override
            public void onCompleted() {
                mView.setProgressVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                mView.onRepositoryErrorOccurred(e);
            }

            @Override
            public void onNext(ProducersListResponseParseData producersList) {
                mView.onProducerListReady(ProducerViewData.fromRaw(producersList.producerData));
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
        final Observable<SearchViewQueryTextEvent> searchViewQueryTextEventObservable = RxSearchView.queryTextChangeEvents(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter(searchViewQueryTextChangeEvent -> {
                    final String queryText = searchViewQueryTextChangeEvent.queryText().toString();
                    //if the view is not resuming and searchText has some input then proceed further
                    final boolean filter = (queryText.length() > 0 && !mView.isResuming());
                    mView.setIsResuming(false);
                    return filter;
                });
        mRepository.startSearchObserving(new IProducerListRepository.Callback<ProducersListResponseParseData>(){

            @Override
            public void onDataObserveStart() {
                mView.setProgressVisibility(View.VISIBLE);
            }
            @Override
            public void onDataUpdated(ProducersListResponseParseData data) {
                mView.setProgressVisibility(View.INVISIBLE);
//                mView.onProducerListReady(data);
            }
            @Override
            public void onListDataUpdated(List<ProducersListResponseParseData> data) {
            }
            @Override
            public void onError(Throwable throwable) {
                mView.setProgressVisibility(View.INVISIBLE);
                mView.onRepositoryErrorOccurred(throwable);
            }
        }, searchViewQueryTextEventObservable);
    }
}
