package sniper.farmdrop.repos;

import com.squareup.sqlbrite.BriteDatabase;

import sniper.farmdrop.api.ApiService;

/**
 * Created by sniper on 01/13/17.
 * If we have Dagger we can Inject those two objects into all repositories
 */

public abstract class BaseRepository {

    protected ApiService mApiService;
    protected BriteDatabase mDataBase;

    public BaseRepository(ApiService mApiService, BriteDatabase mDataBase) {
        this.mApiService = mApiService;
        this.mDataBase = mDataBase;
    }
}
