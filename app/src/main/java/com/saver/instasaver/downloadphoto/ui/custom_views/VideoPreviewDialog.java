package com.saver.instasaver.downloadphoto.ui.custom_views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.listeners.OnItemChangedListener;
import com.saver.instasaver.downloadphoto.ui.custom_views.adaptors.VideoPreviewAdapter;


import java.util.List;

public class VideoPreviewDialog extends Dialog implements GestureDetector.OnGestureListener {

    private List<Media> mMediaList;
    private GestureDetector mDetector;
    private VideoPreviewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mLastPosition;
    private OnItemChangedListener mItemChangedListener;

    public VideoPreviewDialog(Context context, List<Media> mediaList, int lastPosition) {
        super(context);
        this.mMediaList = mediaList;
        this.mLastPosition = lastPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_preview_video);
        init();
        settingUpRecyclerView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        mDetector = new GestureDetector(getContext(), this);
        mAdapter = new VideoPreviewAdapter();
        mAdapter.setOnItemChangedListener(changedPosition -> mItemChangedListener.onItemChanged(changedPosition));
        mAdapter.updateMediaListItems(mMediaList);
        mRecyclerView = findViewById(R.id.rvVideoPreview);
    }

    private void settingUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        lm.scrollToPosition(mLastPosition);
        mRecyclerView.setAdapter(mAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        mRecyclerView.setLayoutManager(lm);
        snapHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        dismiss();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
        if (motionEvent2.getY() - motionEvent1.getY() > 50) {
            dismiss();
        }
        return true;
    }

    public void setOnItemChangedListener(OnItemChangedListener listener) {
        mItemChangedListener = listener;
    }
}
