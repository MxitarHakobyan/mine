package com.saver.instasaver.downloadphoto.instagram_data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media")
public class Media implements Parcelable {

    @NonNull
    @PrimaryKey
    private String mShortCode;

    private String mThumbnailPath;

    private String mPath;

    private boolean mIsVideo;

    private String mTypeName;

    public Media(@NonNull String shortCode, boolean isVideo, String typeName) {
        mShortCode = shortCode;
        mIsVideo = isVideo;
        mTypeName = typeName;
    }

    protected Media(Parcel in) {
        mShortCode = in.readString();
        mPath = in.readString();
        mIsVideo = in.readByte() != 0;
        mTypeName = in.readString();
        mThumbnailPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShortCode);
        dest.writeString(mPath);
        dest.writeByte((byte) (mIsVideo ? 1 : 0));
        dest.writeString(mTypeName);
        dest.writeString(mThumbnailPath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public void setIsVideo(boolean video) {
        mIsVideo = video;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public void setThumbnailPath(String thumbnailPath) {
        mThumbnailPath = thumbnailPath;
    }

    public synchronized String getPath() {
        return mPath;
    }

    public boolean isVideo() {
        return mIsVideo;
    }

    public String getTypeName() {
        return mTypeName;
    }

    @NonNull
    public String getShortCode() {
        return mShortCode;
    }

    public String getThumbnailPath() {
        return mThumbnailPath;
    }
}
