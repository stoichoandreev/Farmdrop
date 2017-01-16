package sniper.farmdrop.ui.views;

import sniper.farmdrop.models.ProducerDetailsViewData;

/**
 * Created by sniper on 01/13/17.
 */

public interface DetailsProducerView extends BaseView{
    void onProducerDetailsReady(ProducerDetailsViewData producerDetails);
    long getProducerId();
}
