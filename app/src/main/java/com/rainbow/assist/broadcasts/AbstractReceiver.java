package com.rainbow.assist.broadcasts;

import android.content.BroadcastReceiver;
import android.util.Log;

/**
 * Created by rajesh on 31/12/17.
 */

public abstract class AbstractReceiver extends BroadcastReceiver {
    private static String tag = AbstractReceiver.class.getName();

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void debug(String message) {
        Log.d(getTag(),message);
    }
}
