package sniper.farmdrop.api.pojos.producers_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sniper on 15-Jan-2017.
 */

public class PaginationParseData {
    @SerializedName("current")
    @Expose
    public Integer current;
    @SerializedName("previous")
    @Expose
    public Integer previous;
    @SerializedName("next")
    @Expose
    public Integer next;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;
    @SerializedName("pages")
    @Expose
    private int pages;
    @SerializedName("count")
    @Expose
    private int count;
}
