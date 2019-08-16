package com.saver.instasaver.downloadphoto.ui.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.helpers.SharedPrefHelper;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.services.SaverService;
import com.saver.instasaver.downloadphoto.ui.activities.adaptors.SectionsPageAdapter;
import com.saver.instasaver.downloadphoto.ui.fragments.PhotoFragment;
import com.saver.instasaver.downloadphoto.ui.fragments.VideoFragment;
import com.saver.instasaver.downloadphoto.utils.FileManager;
import com.saver.instasaver.downloadphoto.utils.PathManager;
import com.saver.instasaver.downloadphoto.view_models.MediaViewModel;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import com.saver.instasaver.downloadphoto.utils.Constants;


@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String SHARED_PREFS = "sharedPref";
    private static final String PHOTO_TITLE = "Photo";
    private static final String VIDEO_TITLE = "Video";
    private static final String INSTAGRAM_PACKAGE = "com.instagram.android";

    private static boolean mIsChecked;

    private Switch mSwitchSaver;
    private SharedPrefHelper mSharedPrefHelper;
    private Intent mIntent;
    private Button mBtnInst;

    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        MainActivityPermissionsDispatcher.initWithPermissionCheck(this);
    }

    private void initTab() {
        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mBtnInst = findViewById(R.id.btnOpenInsta);
        mBtnInst.setEnabled(false);
        setupViewPager();

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager() {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new PhotoFragment(), PHOTO_TITLE);
        adapter.addFragment(new VideoFragment(), VIDEO_TITLE);
        mViewPager.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIsChecked) {
            mBtnInst.setEnabled(mIsChecked);
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void init() {
        mSwitchSaver = findViewById(R.id.switchSaver);
        mSwitchSaver.setChecked(mIsChecked);
        if (mIsChecked) {
            mBtnInst.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
        }
        setListenerOnSwitchSaver();

        mSharedPrefHelper = SharedPrefHelper.createNewInstance(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE));
        PathManager.setDownloadFolder(mSharedPrefHelper.getLoadedData(getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)));

        mIntent = new Intent(MainActivity.this, SaverService.class);
    }

    private void setListenerOnSwitchSaver() {
        mSwitchSaver.setOnCheckedChangeListener((compoundButton, isTurnedOn) -> {
            Intent intent = new Intent(MainActivity.this, SaverService.class);
            if (isTurnedOn) {
                mIsChecked = true;
                mBtnInst.setEnabled(isTurnedOn);
                mBtnInst.setBackgroundColor(getResources().getColor(R.color.colorAccent, getTheme()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(this, intent);
                } else {
                    startService(mIntent);
                }
            } else {
                mIsChecked = false;
                mBtnInst.setEnabled(isTurnedOn);
                mBtnInst.setBackgroundColor(getResources().getColor(R.color.colorAccentTrans, getTheme()));
                stopService(mIntent);
            }
        });
    }

    public void openInstaBtnClicked(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_btn_touch);
        mBtnInst.startAnimation(animation);

        Log.d(TAG, "openInstaBtnClicked: ");
        Uri uri = Uri.parse(Constants.BASE_INSTAGRAM_URL + "_u/");
        Intent instagramIntent = new Intent(Intent.ACTION_VIEW, uri);
        instagramIntent.setPackage(INSTAGRAM_PACKAGE);

        try {
            startActivity(instagramIntent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(Constants.BASE_INSTAGRAM_URL));

            String title = getResources().getString(R.string.browser_chooser_text);

            Intent chooser = Intent.createChooser(intent, title);
            startActivity(chooser);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.permission_alert_dialog_title))
                .setMessage(getResources().getString(R.string.permission_alert_dialog_message))
                .setPositiveButton(getResources().getString(R.string.media_alert_dialog_positive_btn_text), (dialog, which) -> request.proceed())
                .setNegativeButton(getResources().getString(R.string.media_alert_dialog_negative_btn_text), (dialog, which) -> request.cancel())
                .show();
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onPermissionDenied() {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.permission_denied_message), Toast.LENGTH_SHORT).show();

    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onNeverAskAgain() {
        Toast.makeText(MainActivity.this, getResources().getString(R.string.never_ask_again_message), Toast.LENGTH_SHORT).show();
    }

    /**
     * @Callback this is callback method on choose path button
     */
    public void onChoosePathClicked(View view) {
        Button btnPath = view.findViewById(R.id.btnChoosePath);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_btn_touch);
        btnPath.startAnimation(animation);
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, Constants.PATH_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAll) {
            showAlertDialogIsDelete();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * showing dialog it informs that all media will detele
     */
    private void showAlertDialogIsDelete() {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.delete_all_dialog_title))
                .setMessage(getResources().getString(R.string.delete_all_warning_message))
                .setPositiveButton(getResources().getString(R.string.media_alert_dialog_positive_btn_text), (dialogInterface, i) -> {
                    deleteAllMedia();
                    Toast.makeText(this, getResources().getString(R.string.all_media_deleted_message), Toast.LENGTH_LONG).show();
                })
                .setNegativeButton(getResources().getString(R.string.media_alert_dialog_negative_btn_text), (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    /**
     * deletes all media from database and also from external storage
     */
    private void deleteAllMedia() {
        MediaViewModel mediaViewModel = ViewModelProviders.of(MainActivity.this).get(MediaViewModel.class);
        mediaViewModel.getAllMedia().observe(this, mediaList -> {
            if (mediaList.size() > 0) {
                for (Media media : mediaList) {
                    if (media.isVideo()) {
                        FileManager.deleteFile(media.getThumbnailPath());
                    }
                    FileManager.deleteFile(media.getPath());
                    mediaViewModel.deleteMedia(media);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PATH_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mSharedPrefHelper.clear();
                assert data != null;
                mSharedPrefHelper.setData(PathManager.getFolderFromUri(data.getDataString()));
                PathManager.setDownloadFolder(PathManager.getFolderFromUri(data.getDataString()));
                Log.d(TAG, "onActivityResult: " + data.getDataString());
            }
        }
    }
}
