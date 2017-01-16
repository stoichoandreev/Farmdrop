package sniper.farmdrop.repos;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;

/**
 * Created by sniper on 01/16/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsProducerRepositoryTest {

    private DetailsProducerRepository detailsProducerRepository;

    @Mock
    private ProducerDetailsResponseParseData data;
    @Mock
    private TestSubscriber<ProducerDetailsResponseParseData> testSubscriber;

    @Before
    public void setUp() throws Exception {
        detailsProducerRepository = new DetailsProducerRepository();
    }

    @Test
    public void shouldSuccessWhenRequestMovieDetails() throws Exception {
        Observable<ProducerDetailsResponseParseData> movieDetailsObservable = Observable.just(data);//replace our long taking observable with a mocked
        movieDetailsObservable.subscribe(testSubscriber);
        detailsProducerRepository.requestProducerDetails(12);//pass some id
        testSubscriber.assertNoErrors();//no errors
        testSubscriber.assertValue(data);//subscriber has same value from the observable
        testSubscriber.assertCompleted();//subscriber completed
    }
}