package sniper.farmdrop.ui.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import sniper.farmdrop.FarmDropApp;

/**
 * Created by sniper on 18-Dec-2016.
 */

public class AssetsHelper {

    private AssetsHelper() {}

        /**
         * Return the content of a given asset as a plain string.
         *
         * @param assetPath is th e path of the given asset (like "file.json", or "dir/file.json").
         * @return the content of the given asset as a plain string.
         * @throws IOException if needed.
         */
        public static String getStringFromAsset(String assetPath) throws IOException {
            StringBuilder buf = new StringBuilder();
            InputStream inputStream;
            String str;

            try {
                inputStream = FarmDropApp.get().getAssets().open(assetPath);
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

                while ((str = in.readLine()) != null) {
                    buf.append(str);
                }
                inputStream.close();
                in.close();
            } catch (IOException e) {
                throw e;
            }

            return buf.toString();
        }
}
