package com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar;

import com.google.gson.annotations.SerializedName;

import com.saver.instasaver.downloadphoto.utils.Constants;

public class Node {

    @SerializedName("is_video")
    private boolean mIsVideo;

    @SerializedName("display_url")
    private String mImageUrl;

    @SerializedName("shortcode")
    private String mShortcode;

    @SerializedName("video_url")
    private String mVideoUrl;

    public String getTypeFormat() {
        return isVideo() ? Constants.VIDEO_TYPE : Constants.IMAGE_TYPE;
    }

    public boolean isVideo() {
        return mIsVideo;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getShortcode() {
        return mShortcode;
    }
}
