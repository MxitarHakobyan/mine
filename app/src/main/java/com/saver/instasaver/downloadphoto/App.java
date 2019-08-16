package com.saver.instasaver.downloadphoto;

import android.app.Application;

import com.saver.instasaver.downloadphoto.helpers.NotificationHelper;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createNotificationChannel(this);
    }
}
