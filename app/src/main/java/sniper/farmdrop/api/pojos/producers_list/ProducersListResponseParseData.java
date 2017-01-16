package sniper.farmdrop.api.pojos.producers_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sniper on 01/14/17.
 */

public class ProducersListResponseParseData {
    @SerializedName("response")
    @Expose
    public List<ProducerParseData> producerData;
    @SerializedName("count")
    @Expose
    public int count;
    @SerializedName("pagination")
    @Expose
    public PaginationParseData pagination;
}
