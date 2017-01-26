package sniper.farmdrop.api;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;
import sniper.farmdrop.BuildConfig;
import sniper.farmdrop.constants.Preferences;

/**
 * Provides a single instance of {@link Retrofit}.
 */
public class RetrofitServiceProvider {

    private static final String TAG = "RetroFitProvider";
    public static final int CONNECT_TIMEOUT_S = 30;

    private static ApiService instance;

    public static ApiService getApiServiceInstance() {
        if (instance != null) {
            return instance;
        }
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(BuildConfig.RETROFIT_LOG_LEVEL);

        OkHttpClient okHttpClient;
        if(BuildConfig.DEBUG){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
                    //add authentication interceptor if we need
                    .addInterceptor(new LoggingInterceptor())
                    .addInterceptor(interceptor)
//                    .addNetworkInterceptor(new StethoInterceptor())//we can use Stetho only in debug
                    .build();
        }else {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
                    //add authentication interceptor if we need
                    .addInterceptor(new LoggingInterceptor())
                    .addInterceptor(interceptor)
                    .build();
        }

        instance = new Retrofit.Builder()
                .baseUrl(BuildConfig.DEBUG ? Preferences.DEV_API_BASE_URL : Preferences.PROD_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))//Eliminate to put subscribeOn(Schedulers.io)) in every call
                .client(okHttpClient)
                .build().create(ApiService.class);

        return instance;
    }

    public static class LoggingInterceptor implements Interceptor {

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
