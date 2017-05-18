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

public class RetrofitClient {

    private static final String LOG = RetrofitClient.class.getSimpleName();

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, String accessToken) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient(accessToken))
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

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
