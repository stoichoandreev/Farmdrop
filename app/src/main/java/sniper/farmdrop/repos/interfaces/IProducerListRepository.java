package sniper.farmdrop.repos.interfaces;

import com.jakewharton.rxbinding.widget.SearchViewQueryTextEvent;

import java.util.List;

import rx.Observable;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;

/**
 * Data Repository interface about Search Producers inside the list.
 */
public interface IProducerListRepository extends IBaseRepository {
    /**
     * Register search field text change events
     */
    Observable<ProducersListResponseParseData> requestMoreProducersFromAPI(int page, int perPageLimit);
    Observable<List<ProducerLocalCacheData>> getAllLocalCacheProducers(int page);
    void startSearchObserving(Callback<ProducersListResponseParseData> callback, Observable<SearchViewQueryTextEvent> searchViewQueryEventObservable);
}
