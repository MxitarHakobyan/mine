package com.saver.instasaver.downloadphoto.instagram_data.remote.network;

public class NoConnectivityException extends Throwable {
    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}
