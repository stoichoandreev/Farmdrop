package sniper.farmdrop.api.pojos.producers_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sniper on 01/14/17.
 */

public class ProducerParseData {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("permalink")
    @Expose
    public String permalink;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("images")
    @Expose
    public List<ImageParseData> images;
    @SerializedName("short_description")
    @Expose
    public String shortDescription;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("via_wholesaler")
    @Expose
    public boolean viaWholesaler;
    @SerializedName("wholesaler_name")
    @Expose
    public String wholesalerName;
}
