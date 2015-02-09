package com.mariohuete.rss_reader.utils;

import android.os.Environment;

import java.io.File;


/**
 * Created by mariobama on 10/02/15.
 */
public class StoreData {
    public static String FOLDER_DIR = Environment.getExternalStorageDirectory() + "/Lector RSS";

    public static boolean folderExists() {
        // Check if folder exists in SdCard
        return new File(FOLDER_DIR).exists();
    }

    public static void createFolder() {
        // Create new folder in SdCard if doesn't exist
        File f = new File(FOLDER_DIR);
    }

}
