package sniper.farmdrop.repos;


import com.squareup.sqlbrite.BriteDatabase;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import sniper.farmdrop.api.ApiService;
import sniper.farmdrop.api.RetrofitServiceProvider;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;
import sniper.farmdrop.repos.interfaces.IDetailsProducerRepository;

public class DetailsProducerRepository extends BaseRepository implements IDetailsProducerRepository {


    public static final String PRODUCER_ITEM_QUERY = "SELECT * FROM "
            + ProducerLocalCacheData.TABLE
            + " WHERE "
            + ProducerLocalCacheData.PRODUCER_ID
            + " = ?";

    public DetailsProducerRepository(ApiService mApiService, BriteDatabase mDataBase) {
        super(mApiService, mDataBase);
    }

    @Override
    public Observable<ProducerDetailsResponseParseData> requestProducerDetails(long producerId) {
        return mApiService.getProducerDetails(producerId).observeOn(AndroidSchedulers.mainThread());
    }
}
