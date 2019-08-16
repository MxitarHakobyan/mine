package com.saver.instasaver.downloadphoto.instagram_data.remote.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.saver.instasaver.downloadphoto.utils.NetworkUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!NetworkUtil.isOnline(mContext)) {
            try {
                throw new NoConnectivityException();
            } catch (NoConnectivityException e) {
                e.printStackTrace();
            }
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}