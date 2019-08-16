package com.saver.instasaver.downloadphoto.ui.custom_views.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.listeners.OnItemChangedListener;
import com.saver.instasaver.downloadphoto.utils.MediaDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class PhotoPreviewAdapter extends RecyclerView.Adapter<PhotoPreviewAdapter.PreviewViewHolder> {

    private List<Media> mMediaList;
    private Context mContext;
    private OnItemChangedListener mOnItemChangedListener;

    public PhotoPreviewAdapter(Context context) {
        mContext = context;
        mMediaList = new ArrayList<>();
    }

    @NonNull
    @Override
    public PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_preview, parent, false));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PreviewViewHolder holder) {
        mOnItemChangedListener.onItemChanged(holder.getAdapterPosition());
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewViewHolder holder, int position) {
        Glide.with(mContext).load(mMediaList.get(position).getPath()).into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return mMediaList.size();
    }

    public void updateMediaListItems(List<Media> mediaList) {
        final MediaDiffCallback diffCallback = new MediaDiffCallback(this.mMediaList, mediaList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.mMediaList.clear();
        this.mMediaList.addAll(mediaList);
        diffResult.dispatchUpdatesTo(this);
    }

    static class PreviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;

        PreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhotoPreview);
        }
    }

    public void setOnItemChangedListener(OnItemChangedListener listener) {
        mOnItemChangedListener = listener;
    }
}

