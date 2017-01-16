package sniper.farmdrop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sniper.farmdrop.db.models.ProducerLocalCacheData;

/**
 * Created by sniper on 15-Jan-2017.
 */

public final class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    private static final String CREATE_PRODUCER_ITEM_TABLE = ""
            + "CREATE TABLE " + ProducerLocalCacheData.TABLE + "("
            + ProducerLocalCacheData.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + ProducerLocalCacheData.PRODUCER_ID + " INTEGER NOT NULL,"
            + ProducerLocalCacheData.PERMALINK + " TEXT NOT NULL,"
            + ProducerLocalCacheData.NAME + " TEXT NOT NULL,"
            + ProducerLocalCacheData.IMAGE + " TEXT NOT NULL,"
            + ProducerLocalCacheData.SHORT_DESCRIPTION + " TEXT NOT NULL,"
            + ProducerLocalCacheData.DESCRIPTION + " TEXT NOT NULL,"
            + ProducerLocalCacheData.CREATED_AT + " TEXT NOT NULL,"
            + ProducerLocalCacheData.UPDATED_AT + " TEXT NOT NULL,"
            + ProducerLocalCacheData.LOCATION + " TEXT NOT NULL,"
            + ProducerLocalCacheData.VIA_WHOLESALER + " INTEGER NOT NULL DEFAULT 0,"
            + ProducerLocalCacheData.WHOLESALER_NAME + " TEXT NOT NULL,"
            + ProducerLocalCacheData.PAGE + " INTEGER NOT NULL"
            + ")";

    public DBOpenHelper(Context context) {
        super(context, "farmdrop.db", null , VERSION);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCER_ITEM_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
