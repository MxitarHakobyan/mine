package com.saver.instasaver.downloadphoto.utils;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.saver.instasaver.downloadphoto.utils.Constants.BASE_INSTAGRAM_URL;


public class PathManager {

    private static final int SHORT_CODE_PERSISTENCE_INDEX = 4;
    private static final int SHORT_CODE_LENGTH = 11;

    private static String mDownloadFolder;

    public synchronized static String getFilePath(String aType) {
        final String directory = getFolderDirectory();
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return null;
        }
        final File folder = new File(directory);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        String filePath;
        if (success) {
            String videoName = ("File" + getCurSysDate() + aType);
            filePath = directory + File.separator + videoName;
        } else {
            return null;
        }
        return filePath;
    }

    /**
     * @return returns String, current time in static format
     * @Example MM:dd:yyyy-HH:mm:ss
     */
    @SuppressLint("SimpleDateFormat")
    private static String getCurSysDate() {
        return new SimpleDateFormat("HHmmss.SSS").format(new Date());
    }

    private static String getFolderDirectory() {
        return Environment.getExternalStorageDirectory().toString()
                + File.separator
                + getFolderFromUri(PathManager.getDownloadFolder());
    }

    public synchronized static String getFolderFromUri(String encoded) {
        Uri uri = Uri.parse(encoded);
        String dirtyFolderName = uri.getPathSegments().get(uri.getPathSegments().size() - 1);
        if (dirtyFolderName.contains(":")) {
            return dirtyFolderName.substring(dirtyFolderName.indexOf(":") + 1);
        } else {
            return dirtyFolderName;
        }
    }

    /**
     * Video link
     *
     * @return returns String, that contains shortcode
     * @Example url https://www.instagram.com/p/`data shortcode`/?utm_source=ig_web_copy_link
     * photo link
     * @Example https://www.instagram.com/p/`data shortcode`/?utm_source=ig_web_copy_link
     * @Example https://www.instagram.com/p/??/?igshid=2aiw8lapdlhr
     */
    public static String getShortCodeFromUrl(String url) {
        if (url.contains(BASE_INSTAGRAM_URL)) {
            String[] parts = url.split("/");
            if (parts[SHORT_CODE_PERSISTENCE_INDEX].length() == SHORT_CODE_LENGTH) {
                return parts[SHORT_CODE_PERSISTENCE_INDEX];
            }
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("p")) {
                    return parts[i + 1];
                }
            }
        }
        return "";
    }

    /**
     * @return returns String, path that is saved in shared pref
     */
    private static String getDownloadFolder() {
        return mDownloadFolder;
    }

    /**
     * setting up chosen path
     *
     * @param aFolder path that is chosen by user
     */
    public static void setDownloadFolder(String aFolder) {
        PathManager.mDownloadFolder = aFolder;
    }
}
