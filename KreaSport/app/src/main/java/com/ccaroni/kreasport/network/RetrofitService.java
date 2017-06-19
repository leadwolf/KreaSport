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

    public static final String BASE_URL = "http://kreasport.herokuapp.com/";
    public static final String DEBUG_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofit = null;

    public static KreasportAPI getKreasportAPI(boolean debug, String accessToken) {
        if (debug)
            return getClient(DEBUG_URL, accessToken).create(KreasportAPI.class);
        return getClient(BASE_URL, accessToken).create(KreasportAPI.class);
    }

    /**
     * @param baseUrl
     * @param accessToken
     * @return the {@link Retrofit} object that will serve to create an implemetation of our API
     */
    private static Retrofit getClient(String baseUrl, String accessToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient(accessToken))
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
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
