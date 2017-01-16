package sniper.farmdrop.presenters;

import android.view.View;

import rx.Observable;
import rx.Subscriber;
import sniper.farmdrop.FarmDropApp;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.models.ProducerDetailsViewData;
import sniper.farmdrop.repos.DetailsProducerRepository;
import sniper.farmdrop.repos.interfaces.IDetailsProducerRepository;
import sniper.farmdrop.ui.views.DetailsProducerView;

/**
 * Created by sniper on 01/13/17.
 */

public class DetailsProducerPresenter extends BasePresenter<DetailsProducerView, IDetailsProducerRepository> {

    public DetailsProducerPresenter(DetailsProducerView view, DetailsProducerRepository repository){
        super(view, repository);
    }

    public void getProducerDetailsById(){
        if(mView.getProducerId() <= 0){
            mView.onRepositoryErrorOccurred(new Throwable("Please provide producer ID !"));
            return;
        }
        mView.setProgressVisibility(View.VISIBLE);
        final Observable<ProducerDetailsResponseParseData> observable = mRepository.requestProducerDetails(mView.getProducerId());
        addSubscription(observable.subscribe(new Subscriber<ProducerDetailsResponseParseData>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                mView.setProgressVisibility(View.INVISIBLE);
                mView.onRepositoryErrorOccurred((!FarmDropApp.isOnline) ? new Throwable("You seems to be offline") : e);
            }

            @Override
            public void onNext(ProducerDetailsResponseParseData extendedMoveData) {
                mView.setProgressVisibility(View.INVISIBLE);
                mView.onProducerDetailsReady(ProducerDetailsViewData.fromRaw(extendedMoveData));
            }
        }));
    }
}
