package com.kevalpatel2106.home.things;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.kevalpatel2106.home.things.apiai.ApiAiManager;
import com.kevalpatel2106.home.utils.tts.TTS;
import com.kevalpatel2106.pocketsphinx.PocketSphinx;

/**
 * Created by Keval Patel on 05/05/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class SpeechRecognitionService extends Service implements PocketSphinx.Listener {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private ApiAiManager mApiAiManager;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApiAiManager = new ApiAiManager(this);
        PocketSphinx pocketSphinx = new PocketSphinx(this, this);
    }

    @Override
    public void onSpeechRecognizerReady() {
        TTS.speak(this, "I'm ready!");
    }

    @Override
    public void onActivationPhraseDetected() {
        TTS.speak(this, "Yup?");
    }

    @Override
    public void onTextRecognized(String recognizedText) {
        mApiAiManager.send(recognizedText);
    }

    @Override
    public void onTimeout() {
        TTS.speak(this, "Timeout! You're too slow");
    }

    /**
     * A binder object to return to the activity which binds this service.
     */
    public class LocalBinder extends Binder {
        public SpeechRecognitionService getService() {
            // Return this instance of SpeechRecognitionService so clients can call public methods
            return SpeechRecognitionService.this;
        }
    }
}
