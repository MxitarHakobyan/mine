package com.saver.instasaver.downloadphoto.repos;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.saver.instasaver.downloadphoto.instagram_data.async.DatabaseAsyncTask;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.instagram_data.persistence.db.MediaDao;
import com.saver.instasaver.downloadphoto.instagram_data.persistence.db.MediaDatabase;
import com.saver.instasaver.downloadphoto.utils.AppExecutors;

import java.util.List;

import io.reactivex.Maybe;


public class MediaRepo {

    private MediaDao mMediaDao;

    public MediaRepo(Application application) {
        MediaDatabase db = MediaDatabase.getInstance(application);
        mMediaDao = db.getMediaDao();
    }

    public void insertMedia(Media media) {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            mMediaDao.insertMedia(media);
        });
    }

    public void deleteMedia(Media media) {
        AppExecutors.getInstance().getDiskIO().execute(() -> {
            mMediaDao.deleteMedia(media);
        });
    }

    public LiveData<List<Media>> getAllChosenMedia(boolean isVideo) {
        return mMediaDao.getAllChosenMedia(isVideo);
    }

    public Maybe<Media> getMediaObservableByShortCode(String shortCode) {
        return mMediaDao.getMediaObservableByShortCode(shortCode);
    }

    public LiveData<List<Media>> getAllMedia() {
        DatabaseAsyncTask async = new DatabaseAsyncTask();
        async.execute(mMediaDao);
        return async.getMediaListMutableLiveData();
    }
}
