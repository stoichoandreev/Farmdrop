package sniper.farmdrop.api.pojos.producers_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sniper on 15-Jan-2017.
 */

public class ImageParseData {

    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("position")
    @Expose
    public int position;
}
