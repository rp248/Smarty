package com.rainbow.assist.broadcasts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;

import com.rainbow.assist.AppConstants;
import com.rainbow.assist.managers.CallStateManager;
import com.rainbow.assist.managers.MediaManager;
import com.rainbow.assist.services.OutgoingCallTimer;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

public class CallStateReceiver extends AbstractReceiver {


    private void debugCode(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (action != null) {
                debug("Action-"+action);
            } else {
                debug("null action");
            }

            Uri uri = intent.getData();
            if (uri != null) {
                debug("has data");
            }

            Bundle bundle = intent.getExtras();
            Set<String> bundleKeySet = bundle.keySet();
            Iterator<String> iterator = bundleKeySet.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = bundle.get(key).toString();
                debug(key + "-" + value);
            }

            String dataString = intent.getDataString();
            if (dataString != null)
            debug(dataString);

        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        onCallState(context, intent);
    }

    private void onCallState(Context context, Intent intent) {


        String action = intent.getAction();
        debug(action);
        if (action != null) {
            if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                debug("ACTION_NEW_OUTGOING_CALL");
                CallStateManager.getInstance().clearInfo();
                handleOutgoingCall(intent);
            }
            else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
                debug("ACTION_PHONE_STATE_CHANGED");
                handleCallType(context, intent);
            }
            else if (action.equals(OutgoingCallTimer.ACTION_TIMER_FINISHED)) {
                debug("ACTION_TIMER_FINISHED");
                handleOutgoingCallConversation(context);
            }
        }
    }

    private void handleOutgoingCall(Intent intent) {
        String dialedNumber = intent.getExtras().getString(Intent.EXTRA_PHONE_NUMBER);
        CallStateManager.getInstance().setStateInfo(CallStateManager.dialedNumber, dialedNumber);
        CallStateManager.getInstance().setStateInfo(CallStateManager.callType, AppConstants.CALL_OUTGOING);
        debug("handleOutgoingCall");
    }

    private void handleCallType(Context context, Intent intent) {
        debug("handleCallType");
        String dialedNumber = CallStateManager.getInstance().getStateInfo(CallStateManager.dialedNumber);
        String callType = CallStateManager.getInstance().getStateInfo(CallStateManager.callType);
        debug("handleCallType-dialedNumber"+dialedNumber);
        debug("handleCallType-dialedNumber"+AppConstants.CALL_OUTGOING.equals(callType));
        if (dialedNumber != null && callType != null && callType.equals(AppConstants.CALL_OUTGOING)) {
            // We will start timer to find whether its ringing state or users talking.
            //If timer reaches 30 secs plus, we can assume users are talking to each other.
            //If the timer started and before 30 secs we get IDEAL, means call has disconnected.In
            //this case we need to stop the timer.

            handleOutgoingType(context , intent);
        }

    }

    private void handleOutgoingType(Context context, Intent intent) {
        debug("handleOutgoing");
        String callState = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        String incoming_number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (callState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            CallStateManager.getInstance().setStateInfo(CallStateManager.callState, callState);
            CallStateManager.getInstance().setStateInfo(CallStateManager.incomingNumber, incoming_number);
            startTimerService(context);
        }
        else if (callState.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            debug("STOP_RECORDING");

            MediaManager.getINSTANCE().stopRecording();

        }
    }

    private void startTimerService(Context context) {
        debug("startTimerService");
        Intent timerService = new Intent(context, OutgoingCallTimer.class);
        timerService.setAction(OutgoingCallTimer.ACTION_START);
        context.startService(timerService);
    }

    private void handleOutgoingCallConversation(Context context) {
        String callState = CallStateManager.getInstance().getStateInfo(CallStateManager.callState);
        if (callState !=null && callState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            String dialedNumber = CallStateManager.getInstance().getStateInfo(CallStateManager.dialedNumber);
            String incomingNumber = CallStateManager.getInstance().getStateInfo(CallStateManager.incomingNumber);
            debug("handleOutgoingCallConversation-dailedNumber:"+dialedNumber);
            debug("handleOutgoingCallConversation-incomingNumber:"+incomingNumber);
            if (dialedNumber.equals(incomingNumber)) {
                //stopTimerService(context);
                debug("START RECORDING");

                File moviesDir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                File appFolder = new File(moviesDir.getAbsolutePath()+"/.assist");
                MediaManager.getINSTANCE().setPath(appFolder.getPath()+"/"+getFilename(incomingNumber));
                MediaManager.getINSTANCE().init();
                try {
                    MediaManager.getINSTANCE().startRecording();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFilename(String number) {
        //dd-mm-yyyy-hh-mm-ss-number
        return  Calendar.getInstance().getTime().toString()+".3gp";
    }

    private void stopTimerService(Context context) {
        Intent timerService = new Intent(context, OutgoingCallTimer.class);
        timerService.setAction(OutgoingCallTimer.ACTION_STOP);
        context.startService(timerService);
    }
}
