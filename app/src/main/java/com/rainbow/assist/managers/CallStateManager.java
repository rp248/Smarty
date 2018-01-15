package com.rainbow.assist.managers;

import java.util.HashMap;

/**
 * Created by rajesh on 31/12/17.
 */

public class CallStateManager {

    public static String callType = "call_type";
    public static String dialedNumber = "dialed_number";
    public static String timerState = "timer_state";
    public static String recordingState = "recording_state";
    public static String callState = "call_state";
    public static String incomingNumber = "incoming_number";

    public HashMap<String, String> stateMap;

    public static CallStateManager INSTANCE;
    public CallStateManager(){
        stateMap = new HashMap<>();
        INSTANCE = this;
    }

    public static CallStateManager getInstance() {
        return INSTANCE;
    }

    public void setStateInfo(String key, String value) {
        stateMap.put(key, value);
    }

    public String getStateInfo(String key) {
        return stateMap.get(key);
    }

    public void clearInfo() {
        stateMap.clear();
    }
}
