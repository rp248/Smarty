package com.rainbow.assist.managers;

import android.media.MediaRecorder;

import java.io.IOException;

/**
 * Created by rajesh on 8/1/18.
 */

public class MediaManager {
    public static MediaManager INSTANCE;

    private MediaRecorder mediaRecorder;
    private String path;

    public MediaManager() {
        INSTANCE = this;
    }

    public static MediaManager getINSTANCE() {
        return INSTANCE;
    }

    public void init() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        if (path == null) {
            throw new NullPointerException("Path should not be null");
        }else {
            mediaRecorder.setOutputFile(getPath());
        }
        
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void startRecording() throws IOException {
        if (mediaRecorder != null) {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } else {
            throw new IllegalStateException(
                    "init() should be called before calling startRecording()");
        }
    }

    public void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
    }
}
