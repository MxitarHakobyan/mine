package com.saver.instasaver.downloadphoto.ui.fragments.adaptors;

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
import com.saver.instasaver.downloadphoto.listeners.OnItemClickListener;
import com.saver.instasaver.downloadphoto.utils.MediaDiffCallback;

import java.util.ArrayList;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<Media> mMediaList;
    private OnItemClickListener mListener;
    private Context mContext;

    public MediaAdapter(Context context) {
        mMediaList = new ArrayList<>();
        mContext = context;
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        if (mMediaList.get(position).isVideo()) {
            Glide.with(mContext).load(mMediaList.get(position).getThumbnailPath()).centerCrop().into(holder.ivMedia);
        } else {
            Glide.with(mContext).load(mMediaList.get(position).getPath()).centerCrop().into(holder.ivMedia);
        }
    }

    @Override
    public int getItemCount() {
        return mMediaList.size();
    }

    class MediaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMedia;

        MediaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            ivMedia.setOnClickListener(view -> mListener.onItemClicked(mMediaList, getAdapterPosition()));

            ivMedia.setOnLongClickListener(view -> {
                mListener.onItemLongClicked(mMediaList.get(getAdapterPosition()));
                return true;
            });
        }
    }

    public void updateMediaListItems(List<Media> mediaList) {
        final MediaDiffCallback diffCallback = new MediaDiffCallback(this.mMediaList, mediaList);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        diffResult.dispatchUpdatesTo(this);

        this.mMediaList.clear();
        this.mMediaList.addAll(mediaList);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
