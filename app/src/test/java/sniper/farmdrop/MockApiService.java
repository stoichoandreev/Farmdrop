package sniper.farmdrop;

import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import sniper.farmdrop.api.ApiService;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;
import sniper.farmdrop.constants.RequestParameters;

/**
 * Created by sniper on 25-Jan-2017.
 *
 */

public class MockApiService extends BaseMockApiService implements ApiService {

    private MockApiService(MockApiService.Builder builder) {
        mFileName = builder.fileName != null ? builder.fileName : DEFAULT_JSON_FILE_NAME;
    }

    public static final class Builder {
        private String fileName;

        public Builder() {
        }

        public MockApiService.Builder withFileName(String val) {
            fileName = val;
            return this;
        }
        public MockApiService build() {
            return new MockApiService(this);
        }

    }

    /**
     *
     * BELLOW IS GOING TO BE THE FAKE IMPLEMENTATION ABOUT ALL REQUESTS AND THE RESPONSES
     */

    @Override
    public Observable<ProducersListResponseParseData> getProducersList(@Query(RequestParameters.PAGE) int page, @Query(RequestParameters.PER_PAGE_LIMIT) int pageLimit) {
        //If we have diff withFileName provided we need to use it otherwise we will use the default one for this mock response
        return returnMockData(createMockDataObject(ProducersListResponseParseData.class));
    }

    @Override
    public Observable<ProducerDetailsResponseParseData> getProducerDetails(@Path(RequestParameters.ID) long producerID) {
        return returnMockData(createMockDataObject(ProducerDetailsResponseParseData.class));
    }
}
