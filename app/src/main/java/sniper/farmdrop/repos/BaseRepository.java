package sniper.farmdrop.repos;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import sniper.farmdrop.FarmDropApp;
import sniper.farmdrop.api.ApiService;
import sniper.farmdrop.api.RetrofitServiceProvider;

/**
 * Created by sniper on 01/13/17.
 * If we have Dagger we can Inject those two objects into all repositories
 */

public abstract class BaseRepository {
    protected ApiService mApiService = RetrofitServiceProvider.getApiServiceInstance();
    protected BriteDatabase mDataBase = FarmDropApp.getDb();
}
