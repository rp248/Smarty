package com.rainbow.assist.util;

/**
 * Created by rajesh on 8/1/18.
 */

public class AssistUtil {

    public static boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().
                equals(android.os.Environment.MEDIA_MOUNTED);
    }


}
