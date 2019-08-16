package com.saver.instasaver.downloadphoto.listeners;


import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;

import java.util.List;

public interface OnItemClickListener {
    void onItemClicked(List<Media> mediaList, int position);

    void onItemLongClicked(Media media);
}
