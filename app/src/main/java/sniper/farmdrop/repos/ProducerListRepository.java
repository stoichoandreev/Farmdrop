package sniper.farmdrop.repos;

import android.database.sqlite.SQLiteDatabase;

import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import sniper.farmdrop.FarmDropApp;
import sniper.farmdrop.api.ApiService;
import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;
import sniper.farmdrop.repos.interfaces.IProducerListRepository;

public class ProducerListRepository extends BaseRepository implements IProducerListRepository {

    public static final String PRODUCER_LIST_QUERY = "SELECT * FROM " + ProducerLocalCacheData.TABLE;
    public static final String PRODUCER_LIST_BY_PAGE_QUERY = "SELECT * FROM " + ProducerLocalCacheData.TABLE +" WHERE "+ ProducerLocalCacheData.PAGE+" = ?";

    public ProducerListRepository(ApiService mApiService, BriteDatabase mDataBase) {
        super(mApiService, mDataBase);
    }

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
                            .build(), SQLiteDatabase.CONFLICT_REPLACE);
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
