package sniper.farmdrop.presenters;

import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import sniper.farmdrop.models.ProducerDetailsViewData;
import sniper.farmdrop.repos.ProducerListRepository;
import sniper.farmdrop.repos.interfaces.IBaseRepository;
import sniper.farmdrop.repos.interfaces.IProducerListRepository;
import sniper.farmdrop.ui.views.ProducerListView;

import static org.mockito.Mockito.verify;

/**
 * Created by sniper on 01/16/17.
 */
public class ProducerListPresenterTest {
    @Mock
    private ProducerListRepository searchProducerRepository;
    @Mock
    private ProducerListView searchProducerView;
    @Captor
    ArgumentCaptor<IBaseRepository.Callback<ProducerDetailsViewData>> repositoryCallbackCollaborator;
    @Mock
    private IProducerListRepository.Callback<ProducerDetailsViewData> callback;
    private ProducerListPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new ProducerListPresenter(searchProducerView, searchProducerRepository);
    }

    /**
     * Test does Presenter onItemClick method call view method to open Producer Details Screen on position 1
     * @throws Exception
     */
    @Test
    public void shouldProducerClickOpenProducerDetailsOnPosition() throws Exception {
        presenter.onItemClick(Mockito.mock(View.class), 1);
        verify(searchProducerView).openDetailsScreen(1);
    }
}