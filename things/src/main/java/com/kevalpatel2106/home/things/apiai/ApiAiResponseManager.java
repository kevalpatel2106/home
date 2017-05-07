package com.kevalpatel2106.home.things.apiai;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kevalpatel2106.home.things.bluetooth.BluetoothA2DPService;
import com.kevalpatel2106.home.utils.cons.Constants;
import com.kevalpatel2106.home.utils.tts.TTS;

import java.util.Calendar;
import java.util.TimeZone;

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
    private static final String INTENT_TIME_GET = "time.get";

    static void manageResponse(@NonNull final Context context,
                               @NonNull AIResponse aiResponse) {
        final Result result = aiResponse.getResult();
        String speech = result.getFulfillment().getSpeech();
//
//        // Get parameters
//        String parameterString = "";
//        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
//            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
//                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
//            }
//        }
//
//        Log.d("Query:", result.getFulfillment().getSpeech()
//                + "\nAction: " + result.getAction()
//                + "\nParameters: " + parameterString);

        switch (result.getAction()) {
            case INTENT_BT_ON:
            case INTENT_BT_PLAY_SONG:
                BluetoothA2DPService.turnOnBluetooth(context);
                TTS.speak(context, speech);
                break;
            case INTENT_BT_OFF:
                BluetoothA2DPService.turnOffBluetooth(context);
                TTS.speak(context, speech);
                break;
            case INTENT_BT_DISCONNECT:
                BluetoothA2DPService.disconnectBluetooth(context);
                TTS.speak(context, speech);
                break;
            case INTENT_TIME_GET:
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
                //noinspection WrongConstant
                speech = "It is " + calendar.get(Calendar.HOUR)
                        + "," + calendar.get(Calendar.MINUTE)
                        + " " + (calendar.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
                TTS.speak(context, speech);
                break;
            default:
                TTS.speak(context, speech);
                break;
        }
    }
}
