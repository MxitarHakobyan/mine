package com.saver.instasaver.downloadphoto.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.repos.MediaRepo;

import java.util.List;

import io.reactivex.Maybe;

public class MediaViewModel extends AndroidViewModel {

    private MediaRepo mRepository;

    public MediaViewModel(Application application) {
        super(application);
        mRepository = new MediaRepo(application);
    }

    public void insertMedia(Media media) {
        mRepository.insertMedia(media);
    }

    public void deleteMedia(Media media) {
        mRepository.deleteMedia(media);
    }

    public LiveData<List<Media>> getAllChosenMedia(boolean isVideo) {
        return mRepository.getAllChosenMedia(isVideo);
    }

    public LiveData<List<Media>> getAllMedia() {
        return mRepository.getAllMedia();
    }

    public Maybe<Media> getMediaObservableByShortCode(String shortCode) {
        return mRepository.getMediaObservableByShortCode(shortCode);
    }
}
