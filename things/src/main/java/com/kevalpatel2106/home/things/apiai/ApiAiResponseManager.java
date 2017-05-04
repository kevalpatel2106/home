package com.kevalpatel2106.home.things.apiai;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.kevalpatel2106.home.things.bluetooth.BluetoothControlService;

import java.util.Map;

import ai.api.model.AIResponse;
import ai.api.model.Result;

/**
 * Created by Keval on 25-Apr-17.
 */

class ApiAiResponseManager {
    private static final String TAG = ApiAiResponseManager.class.getSimpleName();

    private static final String INTENT_BT_ON = "turnon.bluetooth";
    private static final String INTENT_BT_PLAY_SONG = "play.bluetooth";
    private static final String INTENT_BT_DISCONNECT = "disconnect.bluetooth";
    private static final String INTENT_BT_OFF = "turnoff.bluetooth";

    static void manageResponse(@NonNull final Context context,
                               @NonNull AIResponse aiResponse) {
        final Result result = aiResponse.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        Log.d("Query:", result.getFulfillment().getSpeech()
                + "\nAction: " + result.getAction()
                + "\nParameters: " + parameterString);

        switch (result.getAction()) {
            case INTENT_BT_ON:
            case INTENT_BT_PLAY_SONG:
                BluetoothControlService.turnOnBluetooth(context);
                break;
            case INTENT_BT_OFF:
                BluetoothControlService.turnOffBluetooth(context);
                break;
            case INTENT_BT_DISCONNECT:
                BluetoothControlService.disconnectBluetooth(context);
                break;
        }
    }
}
