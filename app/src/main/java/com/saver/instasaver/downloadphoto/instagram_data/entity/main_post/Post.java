package com.saver.instasaver.downloadphoto.instagram_data.entity.main_post;

import com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar.Edge;
import com.google.gson.annotations.SerializedName;

import com.saver.instasaver.downloadphoto.utils.Constants;

public class Post {

    @SerializedName("is_video")
    private boolean mIsVideo;

    @SerializedName("display_url")
    private String mImageUrl;

    @SerializedName("video_url")
    private String mVideoUrl;

    @SerializedName("__typename")
    private String mTypeName;

    @SerializedName("shortcode")
    private String mShortCode;

    @SerializedName("edge_sidecar_to_children")
    private Edge mEdge;

    public boolean isVideo() {
        return mIsVideo;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public String getTypeFormat() {
        return isVideo() ? Constants.VIDEO_TYPE : Constants.IMAGE_TYPE;
    }

    public Edge getEdge() {
        return mEdge;
    }

    public boolean isSideCar() {
        return this.mTypeName.contains(Constants.SIDECAR_TYPE);
    }


    @Override
    public String toString() {
        return "Post{" +
                "mIsVideo=" + mIsVideo +
                ", mImageList=" + mImageUrl +
                ", mVideoUrl='" + mVideoUrl + '\'' +
                '}';
    }
}
