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

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.listeners.OnItemClickListener;
import com.saver.instasaver.downloadphoto.ui.custom_views.PhotoPreviewDialog;
import com.saver.instasaver.downloadphoto.ui.fragments.adaptors.MediaAdapter;
import com.saver.instasaver.downloadphoto.utils.FileManager;
import com.saver.instasaver.downloadphoto.view_models.MediaViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.saver.instasaver.downloadphoto.utils.Constants;

public class PhotoFragment extends Fragment implements OnItemClickListener {

    private RecyclerView mRvPhoto;
    private MediaAdapter mAdapter;
    private MediaViewModel mViewModel;
    private PhotoPreviewDialog mPreviewDialog;
    private boolean mIsOpen;
    private int mLastPosition;
    private List<Media> mMediaList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mRvPhoto = view.findViewById(R.id.rvPhoto);
        mRvPhoto.setHasFixedSize(true);
        mAdapter = new MediaAdapter(getContext());
        mAdapter.setOnItemClickListener(PhotoFragment.this);
        mViewModel = ViewModelProviders.of(PhotoFragment.this).get(MediaViewModel.class);
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
        mViewModel.getAllChosenMedia(false).observe(getViewLifecycleOwner(), aMediaList -> {
            for (Media media : aMediaList) {
                if (FileManager.isFileNotExist(media.getPath())) {
                    mViewModel.deleteMedia(media);
                }
            }
            mAdapter.updateMediaListItems(aMediaList);
        });
    }

    private void settingUpRecyclerView() {
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        mRvPhoto.setLayoutManager(lm);
        mRvPhoto.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(List<Media> mediaList, int position) {
        preparePreviewDialog(mediaList, position);
    }

    private void preparePreviewDialog(List<Media> mediaList, int position) {
        mIsOpen = true;
        mLastPosition = position;
        mMediaList = mediaList;
        mPreviewDialog = new PhotoPreviewDialog(getContext(), mediaList, position);
        mPreviewDialog.setOnItemChangedListener(changedPosition -> mLastPosition = changedPosition);
        mPreviewDialog.setOnDismissListener(dialogInterface -> {
            mIsOpen = false;
        });
        Objects.requireNonNull(mPreviewDialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        Objects.requireNonNull(mPreviewDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        mPreviewDialog.show();
    }

    @Override
    public void onItemLongClicked(Media media) {
        new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                .setTitle(getResources().getString(R.string.media_alert_dialog_title))
                .setMessage(getResources().getString(R.string.media_alert_dialog_message) + " " +
                        getResources().getString(R.string.is_photo_text))
                .setPositiveButton(getResources().getString(R.string.media_alert_dialog_positive_btn_text), (dialogInterface, i) -> {
                    if (FileManager.deleteFile(media.getPath())) {
                        mViewModel.deleteMedia(media);
                        Toast.makeText(getContext(), getResources().getString(R.string.photo_deleted_message), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.photo_doesnt_exist_message), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.media_alert_dialog_negative_btn_text), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .show();
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
    public void onDestroy() {
        super.onDestroy();
        if (mPreviewDialog != null) {
            mPreviewDialog.dismiss();
        }
    }
}
