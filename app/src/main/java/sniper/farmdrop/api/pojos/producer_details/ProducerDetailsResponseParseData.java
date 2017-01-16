package sniper.farmdrop.api.pojos.producer_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import sniper.farmdrop.api.pojos.producers_list.ProducerParseData;

/**
 * Created by sniper on 01/15/17.
 */

public class ProducerDetailsResponseParseData {
    @SerializedName("response")
    @Expose
    public ProducerParseData producerDetailsParseData;
}
