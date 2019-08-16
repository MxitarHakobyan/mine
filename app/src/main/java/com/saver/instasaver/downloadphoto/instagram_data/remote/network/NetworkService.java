package com.saver.instasaver.downloadphoto.instagram_data.remote.network;

import android.content.Context;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.saver.instasaver.downloadphoto.utils.Constants;

public class NetworkService {
    private static NetworkService sInstance;
    private volatile Retrofit mRetrofit;

    private NetworkService(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_INSTAGRAM_URL)
                .client(new OkHttpClient
                        .Builder()
                        .addInterceptor(new ConnectivityInterceptor(context))
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance(WeakReference<Context> contextWeakReference) {
        if (sInstance == null) {
            synchronized (NetworkService.class) {
                if (sInstance == null) {
                    sInstance = new NetworkService(contextWeakReference.get());
                }
            }
        }
        return sInstance;
    }

    public JsonInstagramApi getJsonApi() {
        return mRetrofit.create(JsonInstagramApi.class);
    }
}
