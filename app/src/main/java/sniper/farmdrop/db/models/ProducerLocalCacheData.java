package sniper.farmdrop.db.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import rx.functions.Func1;
import sniper.farmdrop.db.Db;

/**
 * This Data object will store data for producers into the Local Cache
 */
@AutoValue
public abstract class ProducerLocalCacheData implements Parcelable{

    public static final String TABLE = "producer_item";

    public static final String ID = "_id";
    public static final String PRODUCER_ID = "producer_id";
    public static final String PERMALINK = "permalink";
    public static final String NAME = "name";
    public static final String IMAGE = "image";
    public static final String SHORT_DESCRIPTION = "short_description";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";
    public static final String LOCATION = "location";
    public static final String VIA_WHOLESALER = "via_wholesaler";
    public static final String WHOLESALER_NAME = "wholesaler_name";
    public static final String PAGE = "page";

    public abstract long id();
    public abstract long producerId();
    public abstract String permalink();
    public abstract String name();
    public abstract String image();
    public abstract String createdAt();
    public abstract String updatedAt();
    public abstract String shortDescription();
    public abstract String description();
    public abstract String location();
    public abstract boolean viaWholesaler();
    public abstract String wholesalerName();
    public abstract long page();

    public static final Func1<Cursor, ProducerLocalCacheData> SINGLE_ITEM_MAPPER = (Func1<Cursor, ProducerLocalCacheData>) cursor -> {
        long id = Db.getLong(cursor, ID);
        long producerId = Db.getLong(cursor, PRODUCER_ID);
        String permalink = Db.getString(cursor, PERMALINK);
        String name = Db.getString(cursor, NAME);
        String image = Db.getString(cursor, IMAGE);
        String shortDescription = Db.getString(cursor, SHORT_DESCRIPTION);
        String description = Db.getString(cursor, DESCRIPTION);
        String createdAt = Db.getString(cursor, CREATED_AT);
        String updatedAt = Db.getString(cursor, UPDATED_AT);
        String location = Db.getString(cursor, LOCATION);
        boolean viaWholesaler = Db.getBoolean(cursor, VIA_WHOLESALER);
        String wholesalerName = Db.getString(cursor, WHOLESALER_NAME);
        long page = Db.getLong(cursor, PAGE);
        return new AutoValue_ProducerLocalCacheData(
                id,
                producerId,
                permalink,
                name,
                image,
                shortDescription,
                description,
                createdAt,
                updatedAt,
                location,
                viaWholesaler,
                wholesalerName,
                page);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(ID, id);
            return this;
        }

        public Builder producerId(long producerId) {
            values.put(PRODUCER_ID, producerId);
            return this;
        }

        public Builder permalink(String permalink) {
            values.put(PERMALINK, permalink);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public Builder image(String image) {
            values.put(IMAGE, image);
            return this;
        }

        public Builder shortDescription(String shortDescription) {
            values.put(SHORT_DESCRIPTION, shortDescription);
            return this;
        }

        public Builder description(String description) {
            values.put(DESCRIPTION, description);
            return this;
        }

        public Builder createdAt(String createdAt) {
            values.put(CREATED_AT, createdAt);
            return this;
        }

        public Builder updatedAt(String updatedAt) {
            values.put(UPDATED_AT, updatedAt);
            return this;
        }

        public Builder location(String location) {
            values.put(LOCATION, location);
            return this;
        }

        public Builder viaWholesaler(boolean viaWholesaler) {
            values.put(VIA_WHOLESALER, viaWholesaler);
            return this;
        }

        public Builder wholesalerName(String wholesalerName) {
            values.put(WHOLESALER_NAME, wholesalerName);
            return this;
        }

        public Builder page(long page) {
            values.put(PAGE, page);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
