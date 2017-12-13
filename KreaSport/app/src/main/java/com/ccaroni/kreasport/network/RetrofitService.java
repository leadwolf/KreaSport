package com.ccaroni.kreasport.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Master on 23/04/2017.
 */

public class RetrofitService {

    private static final String LOG = RetrofitService.class.getSimpleName();

    public static final String REMOTE_URL = "http://kreasport.herokuapp.com/";
    public static final String LOCAL_URL = "http://10.0.2.2:8080/";

    public static KreasportAPI getKreasportAPI(boolean useLocalhost, String accessToken) {
        if (useLocalhost) {
            Log.d(LOG, "using localhost server for requests");
            return getClient(LOCAL_URL, accessToken).create(KreasportAPI.class);
        } else {
            Log.d(LOG, "using remote server for requests");
            return getClient(REMOTE_URL, accessToken).create(KreasportAPI.class);
        }
    }

    /**
     * @param baseUrl
     * @param accessToken
     * @return the {@link Retrofit} object that will serve to create an implemetation of our API
     */
    private static Retrofit getClient(String baseUrl, String accessToken) {
        return new Retrofit.Builder()
                .client(getOkHttpClient(accessToken))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * @param accessToken the access token to put in the authorization header
     * @return builds a custom {@link OkHttpClient} with authorization header
     */
    private static OkHttpClient getOkHttpClient(final String accessToken) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Log.d(LOG, "creating request with access token:" + accessToken);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + accessToken)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build();
    }

}
