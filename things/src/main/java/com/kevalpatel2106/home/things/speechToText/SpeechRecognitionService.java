package com.kevalpatel2106.home.things.speechToText;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.kevalpatel2106.home.things.apiai.ApiAiManager;
import com.kevalpatel2106.home.utils.tts.TTS;
import com.kevalpatel2106.pocketsphinx.PocketSphinx;
import com.kevalpatel2106.pocketsphinx.PocketSphinxListener;

/**
 * Created by Keval Patel on 05/05/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class SpeechRecognitionService extends Service implements PocketSphinxListener {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private ApiAiManager mApiAiManager;
    private PocketSphinx pocketSphinx;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApiAiManager = new ApiAiManager(this);
        pocketSphinx = new PocketSphinx(this, this);
    }

    @Override
    public void onSpeechRecognizerReady() {
        TTS.speak(this, "I'm ready!");
        pocketSphinx.startListeningToActivationPhrase();
    }

    @Override
    public void onActivationPhraseDetected() {
        TTS.speak(this, "Yup?");
        pocketSphinx.startListeningToAction();
    }

    @Override
    public void onTextRecognized(String recognizedText) {
        mApiAiManager.send(recognizedText);
        pocketSphinx.startListeningToActivationPhrase();
    }

    @Override
    public void onTimeout() {
        TTS.speak(this, "Timeout! You're too slow");
        pocketSphinx.startListeningToActivationPhrase();
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
