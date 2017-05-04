package com.kevalpatel2106.home.things.apiai;

import android.content.Context;

import com.kevalpatel2106.home.things.BuildConfig;
import com.kevalpatel2106.home.utils.tts.TTS;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Keval on 04-May-17.
 */

public class ApiAiManager implements AIListener {

    private final Context mContext;
    private AIService mAiService;

    public ApiAiManager(Context context) {
        mContext = context;

        //Initialize API.AI Service.
        final AIConfiguration config = new AIConfiguration(BuildConfig.api_ai_access_key,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        mAiService = AIService.getService(context, config);
        mAiService.setListener(this);
    }


    public void send(final String text) {
        //Create observer in Rx.
        final Observer<AIResponse> observer = new Observer<AIResponse>() {
            @Override
            public void onCompleted() {
                //Do nothing
            }

            @Override
            public void onError(Throwable e) {
                ApiAiManager.this.onError(new AIError(e.getMessage()));
            }

            @Override
            public void onNext(AIResponse aiResponse) {
                onResult(aiResponse);
            }
        };

        //Create observable in Rx.
        Observable<AIResponse> observable = Observable.create(new Observable.OnSubscribe<AIResponse>() {
            @Override
            public void call(Subscriber<? super AIResponse> subscriber) {
                try {
                    //Make api request
                    final AIRequest aiRequest = new AIRequest(text);
                    observer.onNext(mAiService.textRequest(aiRequest));
                } catch (AIServiceException e) {
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onResult(AIResponse aiResponse) {
        final Result result = aiResponse.getResult();
        final String speech = result.getFulfillment().getSpeech();
        TTS.speak(speech);

        //Parse and perform specific task
        ApiAiResponseManager.manageResponse(mContext, aiResponse);
    }

    @Override
    public void onError(AIError aiError) {
        String speech = "Sorry, I did not understand what you were saying. Please say it again.";
        TTS.speak(speech);
    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
