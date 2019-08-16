package com.saver.instasaver.downloadphoto.instagram_data.entity.main_post;

import com.google.gson.annotations.SerializedName;

public class Graph {

    @SerializedName("shortcode_media")
    private final Post mPost;

    public Graph(Post post) {
        mPost = post;
    }

    public Post getPost() {
        return mPost;
    }

}
