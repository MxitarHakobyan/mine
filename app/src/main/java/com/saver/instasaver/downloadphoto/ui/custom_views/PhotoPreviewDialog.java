package com.saver.instasaver.downloadphoto.ui.custom_views;

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
import com.saver.instasaver.downloadphoto.ui.custom_views.adaptors.PhotoPreviewAdapter;

import java.util.List;

public class PhotoPreviewDialog extends Dialog implements GestureDetector.OnGestureListener, OnItemChangedListener {
    private List<Media> mMediaList;
    private GestureDetector mDetector;
    private PhotoPreviewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mClickedPosition;
    private OnItemChangedListener mListener;

    public PhotoPreviewDialog(Context context, List<Media> aMediaList, int clickedPosition) {
        super(context);
        this.mMediaList = aMediaList;
        this.mClickedPosition = clickedPosition;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_preview_photo);
        init();
        settingUpRecyclerView();
    }

    private void init() {
        mDetector = new GestureDetector(getContext(), this);
        mAdapter = new PhotoPreviewAdapter(getContext());
        mAdapter.setOnItemChangedListener(PhotoPreviewDialog.this);
        mAdapter.updateMediaListItems(mMediaList);
        mRecyclerView = findViewById(R.id.rvPhotoPreview);
        mRecyclerView.setHasFixedSize(true);
    }

    private void settingUpRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        lm.scrollToPosition(mClickedPosition);
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
        mListener = listener;
    }

    @Override
    public void onItemChanged(int changedPosition) {
        mListener.onItemChanged(changedPosition);
    }
}
