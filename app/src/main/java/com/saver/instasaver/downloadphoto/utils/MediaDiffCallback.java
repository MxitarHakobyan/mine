package com.saver.instasaver.downloadphoto.utils;

import androidx.recyclerview.widget.DiffUtil;

import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;

import java.util.List;

public class MediaDiffCallback extends DiffUtil.Callback {

    private final List<Media> mOldMediaList;
    private final List<Media> mNewMediaList;

    public MediaDiffCallback(List<Media> aOldMediaList, List<Media> aNewMediaList) {
        this.mOldMediaList = aOldMediaList;
        this.mNewMediaList = aNewMediaList;
    }

    @Override
    public int getOldListSize() {
        return mOldMediaList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewMediaList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldMediaList.get(oldItemPosition).getPath().equals(mNewMediaList.get(newItemPosition).getPath());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final Media oldMedia = mOldMediaList.get(oldItemPosition);
        final Media newMedia = mNewMediaList.get(newItemPosition);

        return oldMedia.getPath().equals(newMedia.getPath());
    }
}
