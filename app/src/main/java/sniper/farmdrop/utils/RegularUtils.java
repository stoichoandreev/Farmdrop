package sniper.farmdrop.utils;

import android.support.annotation.Nullable;

/**
 * Created by sniper on 01/13/17.
 */
public class RegularUtils {

    @Nullable
    public static <T> T parseAdditionalParams(Object[] arr, int pos, Class<T> clazz) {
        try {
            return (arr != null && arr.length > 0) ? clazz.cast(arr[pos]) : null;
        } catch(ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
