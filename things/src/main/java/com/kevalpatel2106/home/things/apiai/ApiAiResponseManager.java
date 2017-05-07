package com.kevalpatel2106.home.things.apiai;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.JsonElement;
import com.kevalpatel2106.home.things.bluetooth.BluetoothA2DPService;
import com.kevalpatel2106.home.utils.AudioUtils;
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

    //Bluetooth intents
    private static final String INTENT_BT_ON = "bluetooth.on";
    private static final String INTENT_BT_PLAY_SONG = "bluetooth.play";
    private static final String INTENT_BT_DISCONNECT = "bluetooth.disconnect";
    private static final String INTENT_BT_OFF = "bluetooth.off";

    //Time intents
    private static final String INTENT_TIME_GET = "time.get";

    //Volume intents
    private static final String INTENT_VOLUME_UP = "volume.up";
    private static final String INTENT_VOLUME_DOWN = "volume.down";
    private static final String INTENT_VOLUME_MUTE = "volume.mute";
    private static final String INTENT_VOLUME_UNMUTE = "volume.unmute";
    private static final String INTENT_VOLUME_CHECK = "volume.check";
    private static final String INTENT_VOLUME_MAX = "volume.max";

    static void manageResponse(@NonNull final Context context,
                               @NonNull AIResponse aiResponse) {
        final Result result = aiResponse.getResult();
        String speech = result.getFulfillment().getSpeech();

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
            case INTENT_VOLUME_CHECK:
                speech = "Current volume is " + AudioUtils.getCurrentVolume(context) + "points.";
                TTS.speak(context, speech);
                break;
            case INTENT_VOLUME_UP:
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    if (result.getParameters().get("final-value") != null) {
                        JsonElement element = result.getParameters().get("final-value");
                        AudioUtils.setVolumeTo(context, Integer.parseInt(element.getAsString()));
                    } else if (result.getParameters().get("change-value") != null) {
                        JsonElement element = result.getParameters().get("change-value");
                        AudioUtils.increaseVolumeBy(context, Integer.parseInt(element.getAsString()));
                    } else {
                        AudioUtils.increaseVolumeBy(context, 1);
                    }
                }
                TTS.speak(context, speech);
                break;
            case INTENT_VOLUME_DOWN:
                if (result.getParameters() != null && !result.getParameters().isEmpty()) {
                    if (result.getParameters().get("final-value") != null) {
                        JsonElement element = result.getParameters().get("final-value");
                        AudioUtils.setVolumeTo(context, Integer.parseInt(element.getAsString()));
                    } else if (result.getParameters().get("change-value") != null) {
                        JsonElement element = result.getParameters().get("change-value");
                        AudioUtils.decreaseVolumeBy(context, Integer.parseInt(element.getAsString()));
                    } else {
                        AudioUtils.decreaseVolumeBy(context, 1);
                    }
                }
                TTS.speak(context, speech);
                break;
            case INTENT_VOLUME_MUTE:
                TTS.speak(context, speech);
                AudioUtils.muteVolume(context);
                break;
            case INTENT_VOLUME_UNMUTE:
                AudioUtils.unmuteVolume(context);
                TTS.speak(context, speech);
                break;
            case INTENT_VOLUME_MAX:
                AudioUtils.setMaxVolume(context);
                TTS.speak(context, speech);
                break;
            default:
                TTS.speak(context, speech);
                break;
        }
    }
}
