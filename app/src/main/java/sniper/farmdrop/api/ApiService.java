package sniper.farmdrop.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.constants.EndPoints;
import sniper.farmdrop.constants.RequestParameters;

/**
 * Retrofit Api Service interface.
 */
public interface ApiService {
    /**
     * Get all producers (it has paging)
     * @param page - which page we want
     * @param pageLimit - what is the page range (how much item should include one page)
     * @return - parse data object with producers list in the page
     */
    @GET(EndPoints.PRODUCERS_LIST)
    Observable<ProducersListResponseParseData> getProducersList(@Query(RequestParameters.PAGE) int page, @Query(RequestParameters.PER_PAGE_LIMIT) int pageLimit);

    /**
     * Get producer details data by producer ID
     * @param producerID - producer ID
     * @return - the Observable with parsed producer details data
     */
    @GET(EndPoints.PRODUCERS_DETAILS)
    Observable<ProducerDetailsResponseParseData> getProducerDetails(@Path(RequestParameters.ID) long producerID);
}
