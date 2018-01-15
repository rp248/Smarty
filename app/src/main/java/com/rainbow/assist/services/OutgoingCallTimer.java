package com.rainbow.assist.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class OutgoingCallTimer extends Service {
    public static final String ACTION_START = "com.assist.START_TIMER";
    public static final String ACTION_STOP = "com.assist.STOP_TIMER";
    public static final String ACTION_TIMER_FINISHED = "com.assist.STOP_TIMER_FINISHED";

    private CountDownTimer countDownTimer;

    private boolean isTimerRunning;

    public OutgoingCallTimer() {
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public void setTimerRunning(boolean timerRunning) {
        isTimerRunning = timerRunning;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(ACTION_START)) {
                    startTimer(intent);
                }
                else if (action.equals(ACTION_STOP)) {
                    stopTimer();
                    stopSelf();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer(final Intent intent) {
        stopTimer();
        countDownTimer = new CountDownTimer(50000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isTimerRunning())
                setTimerRunning(true);
            }

            @Override
            public void onFinish() {
                sendStartRecording();
                setTimerRunning(false);
                stopSelf();
            }
        }.start();
    }

    private void sendStartRecording() {
        Intent intent = new Intent();
        intent.setAction(OutgoingCallTimer.ACTION_TIMER_FINISHED);
        sendBroadcast(intent);
    }

    private void stopTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

}
