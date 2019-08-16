package com.saver.instasaver.downloadphoto.instagram_data.entity.edge_sidecar;

import com.google.gson.annotations.SerializedName;

public class HeadNode {

    @SerializedName("node")
    private Node mNode;

    public Node getNode() {
        return mNode;
    }
}
