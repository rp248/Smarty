package com.rainbow.assist;

/**
 * Created by rajesh on 15/1/18.
 */

public class AssistActionParser {
    /**
     * Parses the action, which has to be performed.
     */
    public String parseAction(String speechText) {
        if (speechText != null) {
            if (speechText.contains(AssistActions.ASSIST_CALL))
                return AssistActions.ACTION_CALL;
            if (speechText.contains(AssistActions.ASSIST_DIAL))
                return AssistActions.ASSIST_DIAL;
        }
        return null;
    }
}
