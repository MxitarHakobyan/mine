package com.saver.instasaver.downloadphoto.instagram_data.persistence.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;


@Database(entities = Media.class, exportSchema = false, version = 1)
public abstract class MediaDatabase extends RoomDatabase {
    private static final String DB_NAME = "post.db";
    private static MediaDatabase sInstance;

    public static MediaDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MediaDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(), MediaDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }

    public abstract MediaDao getMediaDao();
}
