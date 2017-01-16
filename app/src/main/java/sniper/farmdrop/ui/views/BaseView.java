package sniper.farmdrop.ui.views;

/**
 * Created by sniper on 01/13/17.
 */

public interface BaseView {
    void onRepositoryErrorOccurred(Throwable error);
    void setProgressVisibility(int visibility);
}
