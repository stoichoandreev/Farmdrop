package sniper.farmdrop;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.CheckForNull;

import rx.Observable;

/**
 * Created by sniper on 25-Jan-2017.
 */

public abstract class BaseMockApiService {

    private final String BASE_PATH_TO_JSON_FILE = "mock_api_json/";
    final String DEFAULT_JSON_FILE_NAME = "empty_response.json";

    String mFileName;

    /**
     * Read Json file and return the json reader
     * @param fileName - some file which contains the mock JSON response
     * @return - JsonReader
     */
    private JsonReader assetJSONFile (@CheckForNull String fileName) {
        InputStream in = getClass().getClassLoader().getResourceAsStream(BASE_PATH_TO_JSON_FILE + fileName);
        InputStreamReader isReader = new InputStreamReader(in);
        return new JsonReader(isReader);
    }

    /**
     * This method will return a Pojo Object (data class object) which will contains all values read from the provided file name
     * @param clazz - the class which need to parse the data from the json
     * @param <T>
     * @return - return instance of the clazz class populated with the data from the json file
     */
    <T> T createMockDataObject(Class<T> clazz){
        final Gson gson = new Gson();
        return gson.fromJson(assetJSONFile(mFileName), clazz);
    }

    /**
     * Use this method to return the result back to the caller
     * @param dataModel - the data model which need to be returner (in our case it will be wrapped around Observable)
     * @param <T>
     * @return - Observable object with dataModel inside( the data model will be populated with data)
     */
    <T>Observable<T>  returnMockData(T dataModel){
        //clear the previous setup about custom file name
        return Observable.just(dataModel);
    }
}
