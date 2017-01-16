package sniper.farmdrop.repos;


import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;
import sniper.farmdrop.repos.interfaces.IDetailsProducerRepository;

public class DetailsProducerRepository extends BaseRepository implements IDetailsProducerRepository {

    public static final String PRODUCER_ITEM_QUERY = "SELECT * FROM "
            + ProducerLocalCacheData.TABLE
            + " WHERE "
            + ProducerLocalCacheData.PRODUCER_ID
            + " = ?";

    @Override
    public Observable<ProducerDetailsResponseParseData> requestProducerDetails(long producerId) {
        return mApiService.getProducerDetails(producerId).observeOn(AndroidSchedulers.mainThread());
    }
}
