package sniper.farmdrop.ui.views;

import java.util.List;

import sniper.farmdrop.models.ProducerViewData;

/**
 * Created by sniper on 01/13/17.
 */

public interface ProducerListView extends BaseView{
    void onProducerListReady(List<ProducerViewData> producersListResult, int page);
    void openDetailsScreen(int position);
    void setIsResuming(boolean isResume);
    boolean isResuming();
}
