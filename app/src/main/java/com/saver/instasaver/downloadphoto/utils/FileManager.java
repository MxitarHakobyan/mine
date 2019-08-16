package com.saver.instasaver.downloadphoto.utils;

import android.net.Uri;

import java.io.File;
import java.util.Objects;

public class FileManager {

    /**
     * deletes selected file from external storage
     *
     * @return returns boolean is deleting was successful
     */
    public static boolean deleteFile(String path) {
        Uri fileUri = Uri.parse(path);
        File file = new File(Objects.requireNonNull(fileUri.getPath()));
        if(file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean isFileNotExist(String filePath) {
        File file = new File(filePath);
        return !file.exists();
    }
}
