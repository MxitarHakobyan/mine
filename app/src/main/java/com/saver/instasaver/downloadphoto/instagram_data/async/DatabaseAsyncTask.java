package com.saver.instasaver.downloadphoto.instagram_data.async;

import android.os.AsyncTask;

import androidx.lifecycle.MutableLiveData;

import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.instagram_data.persistence.db.MediaDao;

import java.util.List;

public class DatabaseAsyncTask extends AsyncTask<MediaDao, Void, List<Media>> {

    private MutableLiveData<List<Media>> mMediaListMutableLiveData = new MutableLiveData<>();

    @Override
    protected List<Media> doInBackground(MediaDao... mediaDao) {
        return mediaDao[0].getAllMedia();
    }

    @Override
    protected void onPostExecute(List<Media> mediaList) {
        mMediaListMutableLiveData.setValue(mediaList);
    }

    public MutableLiveData<List<Media>> getMediaListMutableLiveData() {
        return mMediaListMutableLiveData;
    }
}
