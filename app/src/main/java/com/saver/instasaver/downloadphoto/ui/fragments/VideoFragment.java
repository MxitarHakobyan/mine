package com.saver.instasaver.downloadphoto.ui.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.listeners.OnItemClickListener;
import com.saver.instasaver.downloadphoto.ui.custom_views.VideoPreviewDialog;
import com.saver.instasaver.downloadphoto.ui.fragments.adaptors.MediaAdapter;
import com.saver.instasaver.downloadphoto.utils.Constants;
import com.saver.instasaver.downloadphoto.utils.FileManager;
import com.saver.instasaver.downloadphoto.view_models.MediaViewModel;

public class VideoFragment extends Fragment implements OnItemClickListener {

    private RecyclerView mRvVideo;
    private MediaAdapter mAdapter;
    private MediaViewModel mViewModel;
    private VideoPreviewDialog mPreviewDialog;
    private boolean mIsOpen;
    private int mLastPosition;
    private List<Media> mMediaList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRvVideo = view.findViewById(R.id.rvVideo);
        mRvVideo.setHasFixedSize(true);
        mAdapter = new MediaAdapter(getContext());
        mAdapter.setOnItemClickListener(VideoFragment.this);
        mViewModel = ViewModelProviders.of(VideoFragment.this).get(MediaViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingUpRecyclerView();
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(Constants.IS_OPEN_KEY)) {
                preparePreviewDialog(savedInstanceState.getParcelableArrayList(Constants.MEDIA_LIST_KEY), savedInstanceState.getInt(Constants.LAST_POSITION_KEY));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.getAllChosenMedia(true).observe(getViewLifecycleOwner(), aMediaList -> {
            for (Media media : aMediaList) {
                if (FileManager.isFileNotExist(media.getThumbnailPath())) {
                    mViewModel.deleteMedia(media);
                }
            }
            mAdapter.updateMediaListItems(aMediaList);
        });
    }

    private void settingUpRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        mRvVideo.setLayoutManager(lm);
        mRvVideo.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(List<Media> mediaList, int position) {
        preparePreviewDialog(mediaList, position);
    }

    private void preparePreviewDialog(List<Media> mediaList, int position) {
        mIsOpen = true;
        mLastPosition = position;
        mMediaList = mediaList;
        mPreviewDialog = new VideoPreviewDialog(getContext(), mediaList, position);
        mPreviewDialog.setOnItemChangedListener(changedPosition -> mLastPosition = changedPosition);
        mPreviewDialog.setOnDismissListener(dialogInterface -> {
            mIsOpen = false;
        });
        Objects.requireNonNull(mPreviewDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(mPreviewDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        mPreviewDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mIsOpen) {
            outState.putBoolean(Constants.IS_OPEN_KEY, mIsOpen);
            outState.putInt(Constants.LAST_POSITION_KEY, mLastPosition);
            outState.putParcelableArrayList(Constants.MEDIA_LIST_KEY, (ArrayList<? extends Parcelable>) mMediaList);
        }
    }

    @Override
    public void onItemLongClicked(Media media) {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle(getResources().getString(R.string.media_alert_dialog_title))
                .setMessage(getResources().getString(R.string.media_alert_dialog_message) + " " +
                        getResources().getString(R.string.is_video_text))
                .setPositiveButton(getResources().getString(R.string.media_alert_dialog_positive_btn_text), (dialogInterface, i) -> {
                    if (FileManager.deleteFile(media.getPath()) && FileManager.deleteFile(media.getThumbnailPath())) {
                        mViewModel.deleteMedia(media);
                        Toast.makeText(getContext(), getResources().getString(R.string.video_deleted_message), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.video_doesnt_exist_message), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.media_alert_dialog_negative_btn_text), (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPreviewDialog != null) {
            mPreviewDialog.dismiss();
        }
    }
}
