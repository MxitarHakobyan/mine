package com.saver.instasaver.downloadphoto.instagram_data.remote.network;

import com.saver.instasaver.downloadphoto.instagram_data.entity.main_post.HeadOfResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonInstagramApi {
    @GET("p/{id}/?__a=1")
    Call<HeadOfResponse> getPost(@Path("id") String id);
}
