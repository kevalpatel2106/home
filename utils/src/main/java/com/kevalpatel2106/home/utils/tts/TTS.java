package com.kevalpatel2106.home.utils.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Keval Patel on 25/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class TTS {
    private static final String TAG = TTS.class.getSimpleName();
    private static final String UTTERANCE_ID = "com.kevalpatel2106.home.things.UTTERANCE_ID";

    private static TextToSpeech mTTSEngine;

    public static void init(final Context context) {
        if (mTTSEngine == null) {
            mTTSEngine = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    if (i == TextToSpeech.SUCCESS) {
                        mTTSEngine.setLanguage(Locale.US);
                    } else {
                        mTTSEngine = null;
                    }
                }
            });
        }
    }

    public static void release() {
        if (mTTSEngine != null) {
            mTTSEngine.stop();
            mTTSEngine.shutdown();
            mTTSEngine = null;
        }
    }

    public static void speak(final String text) {
        Log.d(TAG, "Speak : " + text);
        mTTSEngine.speak(text, TextToSpeech.QUEUE_ADD, null, UTTERANCE_ID);
    }
}