package sniper.farmdrop;

import android.app.Application;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;
import sniper.farmdrop.db.DBOpenHelper;


/**
 * Created by sniper on 01/13/17.
 */

public class FarmDropApp extends Application {

    private static FarmDropApp application;
    private static BriteDatabase db;
    public static boolean isOnline = true;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SqlBrite sqlBrite = new SqlBrite.Builder().logger(message -> {
            Log.d("Database", message);
        }).build();
        db = sqlBrite.wrapDatabaseHelper(new DBOpenHelper(application), Schedulers.io());
        db.setLoggingEnabled(true);

    }

    public static FarmDropApp get(){
        return application;
    }
    public static BriteDatabase getDb(){ return db; }
}
