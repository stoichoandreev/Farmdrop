package sniper.farmdrop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import sniper.farmdrop.BR;
import sniper.farmdrop.R;
import sniper.farmdrop.api.pojos.producer_details.ProducerDetailsResponseParseData;
import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;

/**
 * Created by sniper on 01/14/17.
 * This Data class is example how API data model can be wrapped to View Data model with observable,
 * so every time when data model change the view will be updated
 */

public class ProducerDetailsViewData extends BaseObservable {

    private static final String DEFAULT_VALUE = "...";

    private long producerId;
    private String permalink;
    private String name;
    private String image;
    private String createdAt;
    private String updatedAt;
    private String shortDescription;
    private String description;
    private String location;
    private boolean viaWholesaler;
    private String wholesalerName;
    private long page;

    private ProducerDetailsViewData(Builder builder) {
        producerId = builder.producerId;
        permalink = builder.permalink != null ? builder.permalink : DEFAULT_VALUE;
        name = builder.name != null ? builder.name : DEFAULT_VALUE;
        image = builder.image != null ? builder.image : DEFAULT_VALUE;
        createdAt = builder.createdAt != null ? builder.createdAt : DEFAULT_VALUE;
        updatedAt = builder.updatedAt != null ? builder.updatedAt : DEFAULT_VALUE;
        shortDescription = builder.shortDescription != null ? builder.shortDescription : DEFAULT_VALUE;
        description = builder.description != null ? builder.description : DEFAULT_VALUE;
        location = builder.location != null ? builder.location : DEFAULT_VALUE;
        viaWholesaler = builder.viaWholesaler;
        wholesalerName = builder.wholesalerName != null ? builder.wholesalerName : DEFAULT_VALUE;
        page = builder.page;
    }

    /**
     * Use this method to update the View when the new fresh data come from API
     * @param parseObject - the API parse data
     */
    public void updateFrom(ProducerParseData parseObject) {
        setProducerId(new Long(parseObject.id));
        setPermalink(parseObject.permalink);
        setName(parseObject.name);
        setImage(parseObject.images.get(0).path);
        setCreatedAt(parseObject.createdAt);
        setUpdatedAt(parseObject.updatedAt);
        setShortDescription(parseObject.shortDescription);
        setDescription(parseObject.description);
        setLocation(parseObject.location);
        setViaWholesaler(parseObject.viaWholesaler);
        setWholesalerName(parseObject.wholesalerName);
    }
    public static ProducerDetailsViewData fromBasicData(ProducerViewData producerBaseData) {
        if (producerBaseData == null) return null;
        return new Builder()
                .producerId(producerBaseData.getProducerId())
                .name(producerBaseData.getName())
                .shortDescription(producerBaseData.getDescription())
                .build();
    }
    public static ProducerDetailsViewData fromRaw(ProducerDetailsResponseParseData rawProducerData) {
        if (rawProducerData == null || rawProducerData.producerDetailsParseData == null) return null;
        return new Builder()
                .producerId(rawProducerData.producerDetailsParseData.id)
                .permalink(rawProducerData.producerDetailsParseData.permalink)
                .name(rawProducerData.producerDetailsParseData.name)
                .image(rawProducerData.producerDetailsParseData.images != null && rawProducerData.producerDetailsParseData.images.size() > 0 ? rawProducerData.producerDetailsParseData.images.get(0).path : "")
                .createdAt(rawProducerData.producerDetailsParseData.createdAt)
                .updatedAt(rawProducerData.producerDetailsParseData.updatedAt)
                .shortDescription(rawProducerData.producerDetailsParseData.shortDescription)
                .description(rawProducerData.producerDetailsParseData.description)
                .location(rawProducerData.producerDetailsParseData.location)
                .viaWholesaler(rawProducerData.producerDetailsParseData.viaWholesaler)
                .wholesalerName(rawProducerData.producerDetailsParseData.wholesalerName)
                //We don't need any "page" data in the provider details
                .build();
    }
    public static ProducerDetailsViewData fromCache(ProducerLocalCacheData localCacheData) {
        if (localCacheData == null) return null;
        return new Builder()
                .producerId(localCacheData.producerId())
                .permalink(localCacheData.permalink())
                .name(localCacheData.name())
                .image(localCacheData.image())
                .createdAt(localCacheData.createdAt())
                .updatedAt(localCacheData.updatedAt())
                .shortDescription(localCacheData.shortDescription())
                .description(localCacheData.description())
                .location(localCacheData.location())
                .viaWholesaler(localCacheData.viaWholesaler())
                .wholesalerName(localCacheData.wholesalerName())
                //We don't need any "page" data in the provider details
                .build();
    }

    public static final class Builder {
        private long producerId;
        private String permalink;
        private String name;
        private String image;
        private String createdAt;
        private String updatedAt;
        private String shortDescription;
        private String description;
        private String location;
        private boolean viaWholesaler;
        private String wholesalerName;
        private long page;

        public Builder() {
        }

        public ProducerDetailsViewData.Builder producerId(long val) {
            producerId = val;
            return this;
        }
        public ProducerDetailsViewData.Builder permalink(String val) {
            permalink = val;
            return this;
        }

        public ProducerDetailsViewData.Builder name(String val) {
            name = val;
            return this;
        }
        public ProducerDetailsViewData.Builder image(String val) {
            image = val;
            return this;
        }
        public ProducerDetailsViewData.Builder createdAt(String val) {
            createdAt = val;
            return this;
        }
        public ProducerDetailsViewData.Builder updatedAt(String val) {
            updatedAt = val;
            return this;
        }
        public ProducerDetailsViewData.Builder shortDescription(String val) {
            shortDescription = val;
            return this;
        }
        public ProducerDetailsViewData.Builder description(String val) {
            description = val;
            return this;
        }
        public ProducerDetailsViewData.Builder location(String val) {
            location = val;
            return this;
        }
        public ProducerDetailsViewData.Builder viaWholesaler(boolean val) {
            viaWholesaler = val;
            return this;
        }
        public ProducerDetailsViewData.Builder wholesalerName(String val) {
            wholesalerName = val;
            return this;
        }
        public ProducerDetailsViewData.Builder page(long val) {
            page = val;
            return this;
        }

        public ProducerDetailsViewData build() {
            return new ProducerDetailsViewData(this);
        }
    }

    @Bindable
    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
        notifyPropertyChanged(BR.producerId);
    }
    @Bindable
    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
        notifyPropertyChanged(BR.permalink);
    }
    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }
    @Bindable
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        notifyPropertyChanged(BR.image);
    }
    @Bindable
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        notifyPropertyChanged(BR.createdAt);
    }
    @Bindable
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        notifyPropertyChanged(BR.updatedAt);
    }
    @Bindable
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        notifyPropertyChanged(BR.shortDescription);
    }
    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
    @Bindable
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        notifyPropertyChanged(BR.location);
    }
    @Bindable
    public boolean isViaWholesaler() {
        return viaWholesaler;
    }

    public void setViaWholesaler(boolean viaWholesaler) {
        this.viaWholesaler = viaWholesaler;
        notifyPropertyChanged(BR.viaWholesaler);
    }
    @Bindable
    public String getWholesalerName() {
        return wholesalerName;
    }

    public void setWholesalerName(String wholesalerName) {
        this.wholesalerName = wholesalerName;
        notifyPropertyChanged(BR.wholesalerName);
    }

    @Bindable
    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
        notifyPropertyChanged(BR.page);
    }

    //Annotated Binding adapter method must be static
    @BindingAdapter({"bind:image"})
    public static void setImage(ImageView view, String imageUrl) {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(view);
    }
}
