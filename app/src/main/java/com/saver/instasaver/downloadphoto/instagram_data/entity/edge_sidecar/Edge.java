package com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Edge {

    @SerializedName("edges")
    private List<HeadNode> mHeadNodeList;

    public List<HeadNode> getHeadNodeList() {
        return mHeadNodeList;
    }
}
