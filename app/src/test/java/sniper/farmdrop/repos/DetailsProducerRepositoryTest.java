package sniper.farmdrop.repos;

import com.squareup.sqlbrite.BriteDatabase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.observers.TestSubscriber;
import sniper.farmdrop.TestSchedulerRule;
import sniper.farmdrop.api.ApiService;
import sniper.farmdrop.api.RetrofitServiceProvider;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by sniper on 01/16/17.
 */
@RunWith(MockitoJUnitRunner.class)
//@RunWith(PowerMockRunner.class)//We running this Tests with Power Mockito because BriteDatabase is final , and with Mockito is not possible to mock this class
public class DetailsProducerRepositoryTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Rule public TestSchedulerRule testSchedulerRule = new TestSchedulerRule();

    private final int producerTestId = 12;
    private final String testName = "Bobi";
    private final String testDescription = "I am BG farm producer";
    private final String testPermaLink = "http://www.test.com";
    private final String testLocation = "Bulgaria";
    private final String testWholesalerName = "NotSure";

    private DetailsProducerRepository detailsProducerRepository;

    @Mock
    private ApiService apiService;

//    @Mock
    private BriteDatabase dataBase;//keep this null because we don't need it here, we are testing the API Observables

//    @Mock
    private ProducerDetailsResponseParseData data;
    @Mock
    private TestSubscriber<ProducerDetailsResponseParseData> testSubscriber;

    @Before
    public void setUp() throws Exception {
        data = new ProducerDetailsResponseParseData();
        detailsProducerRepository = new DetailsProducerRepository(apiService, dataBase);
        //Add some real data to the Observable
        final ProducerParseData detailsData = new ProducerParseData();
        detailsData.id = producerTestId;
        detailsData.location = testLocation;
        detailsData.description = testDescription;
        detailsData.name = testName;
        detailsData.permalink = testPermaLink;
        detailsData.wholesalerName = testWholesalerName;
        data.producerDetailsParseData = detailsData;
    }

    @Test
    public void shouldSuccessAfterSubscribe() throws Exception {
        when(apiService.getProducerDetails(producerTestId)).thenReturn(Observable.just(data));
        Observable<ProducerDetailsResponseParseData> producerDetailsObservable = detailsProducerRepository.requestProducerDetails(producerTestId);
        producerDetailsObservable.subscribe(testSubscriber);

        testSubscriber.awaitTerminalEvent();

        testSubscriber.assertNoErrors();//no errors
        testSubscriber.assertValueCount(1);//only one object should be inside
        testSubscriber.assertValue(data);//subscriber has same value from the observable
        testSubscriber.assertCompleted();//subscriber completed
    }

    @Test
    public void shouldFindProducerDetails() throws Exception {
        when(apiService.getProducerDetails(producerTestId)).thenReturn(Observable.just(data));
        Observable<ProducerDetailsResponseParseData> producerDetailsObservable = detailsProducerRepository.requestProducerDetails(producerTestId);
        producerDetailsObservable.subscribe(testSubscriber);
        testSubscriber.assertNoErrors();//no errors

        testSubscriber.awaitTerminalEvent();
        testSchedulerRule.getTestScheduler().advanceTimeBy(2, TimeUnit.SECONDS);

//        assertThat(testSubscriber.getOnNextEvents().get(0).producerDetailsParseData)
        assertThat(data.producerDetailsParseData)
                .extracting(ProducerParseData::name)
                .containsExactly(testName);
    }

    @Test
    public void testTimeout() throws Exception {
        when(apiService.getProducerDetails(producerTestId)).thenReturn(Observable.just(data));
        Observable<ProducerDetailsResponseParseData> producerDetailsObservable = detailsProducerRepository.requestProducerDetails(producerTestId);
        producerDetailsObservable.subscribe(testSubscriber);

        //To simulate timeout we are using the rule we are using our TestSchedulerRule,
        // because producerDetailsObservable.timeout will run on computation scheduler
        // and we can replace with our TestScheduler.
        testSchedulerRule.getTestScheduler().advanceTimeBy(RetrofitServiceProvider.CONNECT_TIMEOUT_S + 5, TimeUnit.SECONDS);
//        producerDetailsObservable.timeout(RetrofitServiceProvider.CONNECT_TIMEOUT_S + 5, TimeUnit.SECONDS);

        testSubscriber.assertError(TimeoutException.class);
    }

    @Test
    public void shouldRetry() throws Exception {
        //TODO Need implementation
        //Could follow this Article
        // https://medium.com/@fabioCollini/testing-asynchronous-rxjava-code-using-mockito-8ad831a16877#.7pmar9a8a

    }
}