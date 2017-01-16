package sniper.farmdrop.api;

import android.util.Log;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import sniper.farmdrop.BuildConfig;

/**
 * Created by sniper on 14-Jan-2017.
 */

public class MyLoggingInterceptor implements Interceptor {
    private static final String TAG = "RetroFitLog";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        long t1 = System.nanoTime();
        //Show this logs for debug only
        if(BuildConfig.DEBUG) {
            String requestLog = String.format("Sending request %s on %s%n%s",
                    originalRequest.url(), chain.connection(), originalRequest.headers());
            Log.d(TAG , String.format("Sending request %s on %s%n%s",
                    originalRequest.url(), chain.connection(), originalRequest.headers()));
            if (originalRequest.method().compareToIgnoreCase("post") == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(originalRequest);
            }
            Log.d(TAG, "request" + "\n" + requestLog);
        }

        Response response;
        Request modifiedRequest;
        Request.Builder modifiedRequestBuilder = originalRequest.newBuilder();
        //TODO we can attach some headers to every single request if we need
        modifiedRequest = modifiedRequestBuilder.build();
        response = chain.proceed(modifiedRequest);

        long t2 = System.nanoTime();
        String bodyString = response.body().string();

        if(BuildConfig.DEBUG) {
            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());
            Log.d(TAG, "response" + "\n" + responseLog + "\n" + bodyString);
        }

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "Body-To-String can't be converted";
        }
    }
}
