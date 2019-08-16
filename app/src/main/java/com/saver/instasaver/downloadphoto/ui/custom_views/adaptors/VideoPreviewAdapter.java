package com.saver.instasaver.downloadphoto.ui.custom_views.adaptors;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.saver.instasaver.downloadphoto.R;
import com.saver.instasaver.downloadphoto.instagram_data.entity.Media;
import com.saver.instasaver.downloadphoto.listeners.OnItemChangedListener;
import com.saver.instasaver.downloadphoto.utils.MediaDiffCallback;

public class VideoPreviewAdapter extends RecyclerView.Adapter<VideoPreviewAdapter.PreviewViewHolder> {
    private static final String TAG = "VideoPreviewAdapter";
    private List<Media> mMediaList;
    private OnItemChangedListener mOnItemChangedListener;

    public VideoPreviewAdapter() {
        mMediaList = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoPreviewAdapter.PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoPreviewAdapter.PreviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_preview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoPreviewAdapter.PreviewViewHolder holder, int position) {
        holder.videoView.setVideoURI(Uri.fromFile(new File(mMediaList.get(position).getPath())));
        holder.videoView.start();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull PreviewViewHolder holder) {
        holder.videoView.start();
        mOnItemChangedListener.onItemChanged(holder.getAdapterPosition());
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
        VideoView videoView;
        ImageView ivPlay;

        PreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            ivPlay = itemView.findViewById(R.id.ivPlayBtn);
            videoView.setOnClickListener(view -> {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivPlay.setVisibility(View.VISIBLE);

                } else {
                    videoView.start();
                    ivPlay.setVisibility(View.GONE);
                }
            });

            videoView.setOnInfoListener((mediaPlayer, i, i1) -> {
                if (mediaPlayer.isPlaying()) {
                    ivPlay.setVisibility(View.GONE);
                }
                Log.d(TAG, "PreviewViewHolder: " + mediaPlayer.isPlaying());
                return true;
            });
        }
    }

    public void setOnItemChangedListener(OnItemChangedListener listener) {
        mOnItemChangedListener = listener;
    }
}
