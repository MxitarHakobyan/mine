package com.saver.instasaver.downloadphoto.instagram_data.entity.main_post;

import com.google.gson.annotations.SerializedName;

public class HeadOfResponse {

    @SerializedName("graphql")
    private final Graph mGraph;

    public HeadOfResponse(Graph graph) {
        mGraph = graph;
    }

    public Graph getGraph() {
        return mGraph;
    }
}
