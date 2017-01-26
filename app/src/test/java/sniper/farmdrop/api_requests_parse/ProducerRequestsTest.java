package sniper.farmdrop.api_requests_parse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;
import sniper.farmdrop.MockApiService;
import sniper.farmdrop.api.pojos.producers_list.ProducersListResponseParseData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyInt;

/**
 * Created by sniper on 26-Jan-2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProducerRequestsTest {

    @Test
    public void testProducersListParseDataWithOneItem() throws Exception {
        MockApiService mockApiService = new MockApiService.Builder()
                .withFileName("producer_list_response_200_with_one_pr.json")
                .build();
        Observable<ProducersListResponseParseData> mockProducerList = mockApiService.getProducersList(anyInt(), anyInt());
        assertThat(mockProducerList).isNotNull();
        //check the producers list parse data
        mockProducerList
                .subscribe(producersListResponseParseData -> {
                    assertThat(producersListResponseParseData.count).isBetween(0, 10);
                    assertThat(producersListResponseParseData.pagination).isNotNull();
                    assertThat(producersListResponseParseData.producerData).isNotNull();
                    assertThat(producersListResponseParseData.producerData).hasSize(1);
                });
        //check the single producers data in the list
        mockProducerList
                .flatMap(producersListResponseParseData -> Observable.from(producersListResponseParseData.producerData))
                .subscribe(producerParseData -> {
                   assertThat(producerParseData).isNotNull();
                   assertThat(producerParseData.id).isEqualTo(170);
                   assertThat(producerParseData.name).isNotNull();
                   assertThat(producerParseData.name).isEqualTo("Purton House Organics");
                    //We can continue here with some more data checks
                });
    }
    @Test
    public void testProducersListParseDataWithManyItems() throws Exception {
        MockApiService mockApiService = new MockApiService.Builder()
                .withFileName("producer_list_response_200_with_many_pr.json")
                .build();
        Observable<ProducersListResponseParseData> mockProducerList = mockApiService.getProducersList(anyInt(), anyInt());
        assertThat(mockProducerList).isNotNull();
    }
}
