package sniper.farmdrop.repos;

import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import sniper.farmdrop.FarmDropApp;
import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;
import sniper.farmdrop.repos.interfaces.IProducerListRepository;

public class ProducerListRepository extends BaseRepository implements IProducerListRepository {

    public static final String PRODUCER_LIST_QUERY = "SELECT * FROM " + ProducerLocalCacheData.TABLE;
    public static final String PRODUCER_LIST_BY_PAGE_QUERY = "SELECT * FROM " + ProducerLocalCacheData.TABLE +" WHERE "+ ProducerLocalCacheData.PAGE+" = ?";

    @Override
    public Observable<ProducersListResponseParseData> requestMoreProducersFromAPI(int page, int perPageLimit) {
        return mApiService.getProducersList(page, perPageLimit)
                .observeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.computation())
                // Write to the Cache on Computation scheduler
                .map(this::writeToLocalCache);
//                .observeOn(AndroidSchedulers.mainThread());
//                .map(this::getAllLocalCacheProducers);
        // Read any cached results
//        Observable<List<ProducerLocalCacheData>> cachedProviders = getAllLocalCacheProducers(page);
//        if (cachedProviders != null){
//            // Merge with the observable from API
//            observable = observable.mergeWith(Observable.just(cachedProviders));
//        }
//        return observable;
    }

    @Override
    public Observable<List<ProducerLocalCacheData>> getAllLocalCacheProducers(int page) {
        return mDataBase.createQuery(ProducerLocalCacheData.TABLE, PRODUCER_LIST_BY_PAGE_QUERY, Integer.toString(page))
                .mapToList(ProducerLocalCacheData.SINGLE_ITEM_MAPPER);
    }

    @Override
    public void startSearchObserving(Callback<ProducersListResponseParseData> callback, Observable<SearchViewQueryTextEvent> searchViewQueryEventObservable) {
        searchViewQueryEventObservable.map(searchQueryEventObservable -> searchQueryEventObservable.queryText().toString())
                .switchMap(new Func1<String, Observable<ProducersListResponseParseData>>() {
                    @Override
                    public Observable<ProducersListResponseParseData> call(String s) {
                        callback.onDataObserveStart();//send callback to display the progress
                        return mApiService.getProducersList(1, 10).onErrorResumeNext(Observable.empty());//subscription will be terminated once an error is emitted, we should avoid that
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getRxSearchObserver(callback));
    }
    private Observer<ProducersListResponseParseData> getRxSearchObserver(Callback<ProducersListResponseParseData> callback) {
        return new Observer<ProducersListResponseParseData>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                callback.onError((!FarmDropApp.isOnline) ? new Throwable("You seems to be offline") : e);
            }

            @Override
            public void onNext(ProducersListResponseParseData parseData) {
                callback.onDataUpdated(parseData);
            }
        };
    }
    private ProducersListResponseParseData writeToLocalCache(ProducersListResponseParseData producersParseResponse) {
        //check do we have something to insert/update
        boolean result = (producersParseResponse != null && producersParseResponse.producerData != null && producersParseResponse.producerData.size() > 0);
        final int page = (producersParseResponse != null && producersParseResponse.pagination != null ? (producersParseResponse.pagination.current != null ? producersParseResponse.pagination.current : 1) : 0);
        if(result) {
            BriteDatabase.Transaction transaction = mDataBase.newTransaction();
            try {
                for(ProducerParseData producer: producersParseResponse.producerData) {
                    mDataBase.insert(ProducerLocalCacheData.TABLE,
                            new ProducerLocalCacheData.Builder()
                                    .producerId(producer.id)
                                    .permalink(producer.permalink)
                                    .name(producer.name)
                                    .image(producer.images != null && producer.images.size() > 0 ? producer.images.get(0).path : null)
                                    .shortDescription(producer.shortDescription)
                                    .description(producer.description)
                                    .createdAt(producer.createdAt)
                                    .updatedAt(producer.updatedAt)
                                    .location(producer.location)
                                    .viaWholesaler(producer.viaWholesaler)
                                    .wholesalerName(producer.name)
                                    .page(page)
                            .build());
                }
                transaction.markSuccessful();
                result = true;
            } catch (Exception ex){
                ex.printStackTrace();
                result = false;
            } finally {
                transaction.end();
            }
        }
        return producersParseResponse;
    }
}
