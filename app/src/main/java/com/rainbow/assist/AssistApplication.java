package com.rainbow.assist;

import android.app.Application;
import android.os.Environment;

import com.rainbow.assist.managers.CallStateManager;
import com.rainbow.assist.managers.MediaManager;

import java.io.File;

/**
 * Created by rajesh on 31/12/17.
 */

public class AssistApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        new CallStateManager();
        new MediaManager();
        //boolean isAva = AssistUtil.isExternalStorageAvailable();
        //Log.d("AssistApplication", isAva+"");
        createFolder();
    }

    private void createFolder() {
        if (!isAppFolderAvailable()) {
            File moviesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            File appFolder = new File(moviesDir+"/.assist");
            boolean b = appFolder.mkdir();
        }
    }

    private boolean isAppFolderAvailable() {
        File moviesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File appFolder = new File(moviesDir.getAbsolutePath()+"/.assist");
        ///storage/emulated/0/Android/data/com.rainbow.assist/files/Movies/.assist
        return appFolder.exists();
    }
}
