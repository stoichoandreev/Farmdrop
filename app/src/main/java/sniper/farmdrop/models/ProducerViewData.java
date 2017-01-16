package sniper.farmdrop.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import sniper.farmdrop.BR;
import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;
import sniper.farmdrop.db.models.ProducerLocalCacheData;

/**
 * Created by sniper on 01/14/17.
 * This Data class is example how API data model can be wrapped to View Data model with observable,
 * so every time when view data model change the view will be updated
 */

public class ProducerViewData extends BaseObservable implements Serializable{

    private static final String DEFAULT_VALUE = "...";

    private long producerId;
    private String name;
    private String description;

    public ProducerViewData(){}

    private ProducerViewData(Builder builder) {
        name = builder.name != null ? builder.name : DEFAULT_VALUE;
        description = builder.description != null ? builder.description : DEFAULT_VALUE;
        producerId = builder.producerId;
    }

    public static List<ProducerViewData> fromRaw(List<ProducerParseData> producerRawList) {
        if (producerRawList == null) return null;
        List<ProducerViewData> viewDataList = new ArrayList<>(producerRawList.size());

        for (ProducerParseData rawProducer : producerRawList) {
            viewDataList.add(new Builder()
                    .producerId(rawProducer.id)
                    .name(rawProducer.name)
                    .description(rawProducer.shortDescription)
                    .build());
        }
        return viewDataList;
    }
    public static List<ProducerViewData> fromCache(List<ProducerLocalCacheData> producerCacheList) {
        if (producerCacheList == null) return null;
        List<ProducerViewData> viewDataList = new ArrayList<>(producerCacheList.size());

        for (ProducerLocalCacheData cacheProducer : producerCacheList) {
            viewDataList.add(new Builder()
                    .producerId(cacheProducer.id())
                    .name(cacheProducer.name())
                    .description(cacheProducer.shortDescription())
                    .build());
        }
        return viewDataList;
    }

    public static final class Builder {
        private long producerId;
        private String name;
        private String description;

        public Builder() {}

        public Builder name(String val) {
            this.name = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder producerId(long val) {
            producerId = val;
            return this;
        }

        public ProducerViewData build() {
            return new ProducerViewData(this);
        }
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }
    @Bindable
    public long getProducerId() {
        return producerId;
    }

    public void setProducerId(long producerId) {
        this.producerId = producerId;
        notifyPropertyChanged(BR.producerId);
    }
}
