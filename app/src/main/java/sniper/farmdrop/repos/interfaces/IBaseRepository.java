package sniper.farmdrop.repos.interfaces;

import java.util.List;

/**
 * Data Repository interface.
 */
public interface IBaseRepository {

    interface Callback<T> {
        void onDataObserveStart();
        void onDataUpdated(T data);
        void onListDataUpdated(List<T> data);
        void onError(Throwable throwable);
    }

}
