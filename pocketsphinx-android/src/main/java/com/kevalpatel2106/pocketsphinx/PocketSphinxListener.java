package com.kevalpatel2106.pocketsphinx;

/**
 * Created by Keval on 05-May-17.
 */

public interface PocketSphinxListener {
    void onSpeechRecognizerReady();

    void onActivationPhraseDetected();

    void onTextRecognized(String recognizedText);

    void onTimeout();
}
