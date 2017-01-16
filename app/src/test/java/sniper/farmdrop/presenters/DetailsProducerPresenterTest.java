package sniper.farmdrop.presenters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import sniper.farmdrop.models.ProducerDetailsViewData;
import sniper.farmdrop.repos.DetailsProducerRepository;
import sniper.farmdrop.repos.interfaces.IBaseRepository;
import sniper.farmdrop.repos.interfaces.IDetailsProducerRepository;
import sniper.farmdrop.ui.views.DetailsProducerView;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by sniper on 01/16/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class DetailsProducerPresenterTest {

    private static final int PRODUCER_MOCK_ID = 123;

    @Mock
    private DetailsProducerRepository detailsProducerRepository;
    @Mock
    private DetailsProducerView detailsProducerView;
    @Captor
    ArgumentCaptor<IBaseRepository.Callback<ProducerDetailsViewData>> repositoryCallbackCollaborator;
    @Mock
    private IDetailsProducerRepository.Callback<ProducerDetailsViewData> callback;
    private DetailsProducerPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new DetailsProducerPresenter(detailsProducerView, detailsProducerRepository);
    }

    /**
     * Test the view error callback for invalid producerId
     * @throws Exception
     */
    @Test
    public void shouldShowErrorMessageIfProducerIdDoesNotExist() throws Exception {
        when(detailsProducerView.getProducerId()).thenReturn(0L);
        presenter.getProducerDetailsById();
        verify(detailsProducerView).onRepositoryErrorOccurred(isA(Throwable.class));
    }
}