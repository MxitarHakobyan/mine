package com.saver.instasaver.downloadphoto.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.helpers.DownloadHelper;
import com.saver.instasaver.downloadphoto.helpers.NotificationHelper;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar.HeadNode;
import com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar.Node;
import com.saver.instasaver.downloadphoto.instagram_data.entity.main_post.HeadOfResponse;
import com.saver.instasaver.downloadphoto.instagram_data.entity.main_post.Post;
import com.saver.instasaver.downloadphoto.instagram_data.remote.network.NetworkService;
import com.saver.instasaver.downloadphoto.instagram_data.remote.network.NoConnectivityException;
import com.saver.instasaver.downloadphoto.utils.AppExecutors;
import com.saver.instasaver.downloadphoto.utils.Constants;
import com.saver.instasaver.downloadphoto.utils.PathManager;
import com.saver.instasaver.downloadphoto.view_models.MediaViewModel;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaverService extends Service implements ClipboardManager.OnPrimaryClipChangedListener {

    private static final int PERSISTENCE_NOTIFICATION_ID = 2;
    private static final String TAG = "SaverService";

    private NotificationManagerCompat mNotificationManager;
    private NotificationCompat.Builder mPersistenceNotification;
    private ClipboardManager mClipboard;
    private Post mPost;
    private MediaViewModel mViewModel;
    private DownloadHelper mDownloadHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mNotificationManager = (NotificationManagerCompat.from(SaverService.this));
        mPersistenceNotification = NotificationHelper.getPersistenceNotification(SaverService.this);
        mClipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        mViewModel = new MediaViewModel(getApplication());
        mDownloadHelper = DownloadHelper.createNewInstance(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(PERSISTENCE_NOTIFICATION_ID, mPersistenceNotification.build());
        } else {
            mNotificationManager.notify(PERSISTENCE_NOTIFICATION_ID, mPersistenceNotification.build());
        }
        setListenerOnClipboard();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListenerOnClipboard();
        stopForeground(true);
        stopSelf();
    }

    private void setListenerOnClipboard() {
        if (mClipboard != null) {
            mClipboard.addPrimaryClipChangedListener(this);
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPrimaryClipChanged() {
        String url = Objects.requireNonNull(mClipboard.getPrimaryClip()).getItemAt(0).getText().toString();
        Log.d(TAG, "onPrimaryClipChanged: " + url);
        String shortCode = PathManager.getShortCodeFromUrl(url);
        getData(shortCode);
    }

    /**
     * method requests to instagram server and retrieve post's data
     *
     * @param shortCode data shortCode from copied url
     */
    private void getData(String shortCode) {
        NetworkService.getInstance(new WeakReference<>(getApplicationContext()))
                .getJsonApi()
                .getPost(shortCode)
                .enqueue(new Callback<HeadOfResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<HeadOfResponse> call, @NonNull Response<HeadOfResponse> response) {
                        if (!response.isSuccessful()) {
                            Log.d(TAG, "onResponse: Code:" + response.code());
                            Toast.makeText(SaverService.this, "Problem with server", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Log.d(TAG, "onResponse: Url:= " + "https://www.instagram.com/p/" + shortCode + "/?__a=1");
                        if (response.body() != null) {
                            mPost = response.body().getGraph().getPost();
                            if (mPost.isSideCar()) {
                                for (HeadNode headNode : mPost.getEdge().getHeadNodeList()) {
                                    processingOnlyNotExistsMedia(headNode.getNode(), headNode.getNode().getShortcode());
                                }
                            } else {
                                processingOnlyNotExistsMedia(mPost, shortCode);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<HeadOfResponse> call, @NonNull Throwable t) {
                        Log.d(TAG, "onFailure: Message:" + t.getMessage());
                        if (t instanceof NoConnectivityException) {
                            Toast.makeText(SaverService.this, "No Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void processingOnlyNotExistsMedia(Node node, String shortcode) {
        mViewModel.getMediaObservableByShortCode(shortcode)
                .subscribeOn(Schedulers.single())
                .subscribe(new DisposableMaybeObserver<Media>() {
                    @Override
                    public void onSuccess(Media media) {
                        showExistsToast();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Media media = createMedia(node);
                        if (node.isVideo()) {
                            media.setThumbnailPath(PathManager.getFilePath(Constants.IMAGE_TYPE));
                            downloadMedia(media, node.getVideoUrl(), node.getImageUrl());
                        } else {
                            downloadMedia(media, node.getImageUrl());
                        }
                    }
                });
    }

    private void processingOnlyNotExistsMedia(Post post, String shortcode) {
        mViewModel.getMediaObservableByShortCode(shortcode)
                .subscribeOn(Schedulers.single())
                .subscribe(new DisposableMaybeObserver<Media>() {
                    @Override
                    public void onSuccess(Media media) {
                        showExistsToast();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Media media = createMedia(post);
                        if (post.isVideo()) {
                            media.setThumbnailPath(PathManager.getFilePath(Constants.IMAGE_TYPE));
                            downloadMedia(media, post.getVideoUrl(), post.getImageUrl());
                        } else {
                            downloadMedia(media, post.getImageUrl());
                        }
                    }
                });
    }

    private void showExistsToast() {
        AppExecutors.getInstance()
                .getMainThread()
                .execute(() -> Toast.makeText(getApplicationContext(),
                getResources().getString(R.string.media_already_exists_message),
                Toast.LENGTH_LONG).show());
    }

    private Media createMedia(Node node) {
        Media sideCarMedia = new Media(node.getShortcode(), node.isVideo(), node.getTypeFormat());
        sideCarMedia.setPath(PathManager.getFilePath(node.getTypeFormat()));
        return sideCarMedia;
    }

    private Media createMedia(Post post) {
        Media media = new Media(post.getShortCode(), post.isVideo(), post.getTypeName());
        media.setPath(PathManager.getFilePath(post.getTypeFormat()));
        return media;
    }

    private void downloadMedia(@NonNull Media media, String url) {
        media.setIsVideo(false);
        mDownloadHelper.downloadData(url, new File(media.getPath()));
        mViewModel.insertMedia(media);
    }

    private void downloadMedia(@NonNull Media media, String url, String thumbnailUrl) {
        media.setIsVideo(true);
        mDownloadHelper.downloadData(thumbnailUrl, new File(media.getThumbnailPath()));
        mDownloadHelper.downloadData(url, new File(media.getPath()));
        mViewModel.insertMedia(media);
    }

    private void removeListenerOnClipboard() {
        if (mClipboard != null) {
            mClipboard.removePrimaryClipChangedListener(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}