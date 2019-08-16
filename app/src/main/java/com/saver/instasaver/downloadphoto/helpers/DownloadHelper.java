package com.saver.instasaver.downloadphoto.helpers;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import java.io.File;

public class DownloadHelper {

    private final DownloadManager mDownloadManager;

    private DownloadHelper(Context context) {
        this.mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public static DownloadHelper createNewInstance(Context context) {
        return new DownloadHelper(context);
    }

    public void downloadData(String url, File file) {
        Uri downloadUri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(true);
        request.setAllowedOverMetered(true);
        request.setDestinationUri(Uri.fromFile(file));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("Downloading");
        request.setDescription("Downloading File");
        request.allowScanningByMediaScanner();
        mDownloadManager.enqueue(request);
    }
}
