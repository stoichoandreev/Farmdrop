package sniper.farmdrop.repos.interfaces;

import rx.Observable;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;

/**
 * Data Repository interface about DetailsProducerView.
 * It is responsible to deliver selected producer details data
 */
public interface IDetailsProducerRepository extends IBaseRepository {
    /**
     * Get producer details data by producer ID
     * @param producerID - ID if the producer
     */
    Observable<ProducerDetailsResponseParseData> requestProducerDetails(long producerID);
}
