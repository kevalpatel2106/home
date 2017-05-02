package com.kevalpatel2106.home.utils.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by Keval Patel on 25/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class TTS {

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
        if (mTTSEngine != null) mTTSEngine.shutdown();
    }

    public static void speak(final String text) {
        mTTSEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}