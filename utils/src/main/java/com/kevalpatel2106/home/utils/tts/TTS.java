package com.kevalpatel2106.home.utils.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;

/**
 * Created by Keval Patel on 25/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class TTS {

    private static TextToSpeech textToSpeech;

    public static void init(final Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {

                }
            });
        }
    }

    public static void release() {
        if (textToSpeech != null) textToSpeech.shutdown();
    }

    public static void speak(final String text) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}