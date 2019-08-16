package com.saver.instasaver.downloadphoto.instagram_data.persistence.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface MediaDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMedia(Media media);

    @Delete
    void deleteMedia(Media media);

    @Query("SELECT * FROM media WHERE mShortCode = :shortCode")
    Maybe<Media> getMediaObservableByShortCode(String shortCode);

    @Query("SELECT * FROM media")
    List<Media> getAllMedia();

    @Query("SELECT * FROM media WHERE mIsVideo = :isVideo")
    LiveData<List<Media>> getAllChosenMedia(boolean isVideo);
}
