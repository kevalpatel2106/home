package com.kevalpatel2106.home.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.JsonElement;
import com.kevalpatel2106.home.BuildConfig;
import com.kevalpatel2106.home.R;
import com.kevalpatel2106.home.base.BaseActivity;
import com.kevalpatel2106.home.utils.tts.TTS;

import java.util.Map;

import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatRoomActivity extends BaseActivity
        implements AIListener, TextWatcher {
    private static final int REQ_CODE_AUDIO_PERMISSION = 7872;

    @BindView(R.id.send_command_btn)
    FloatingActionButton mSendCommandBtn;

    @BindView(R.id.et_command)
    AppCompatEditText mCommandEt;

    @BindView(R.id.command_switcher)
    ViewSwitcher mCommandBoxSwitcher;

    @BindView(R.id.command_tv)
    AppCompatTextView mCommandTv;

    @BindView(R.id.response_tv)
    AppCompatTextView mResponseTv;

    private boolean isListening = false;
    private boolean isProcessing = false;

    private AIService mAiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            init();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQ_CODE_AUDIO_PERMISSION);
        }
    }

    /**
     * Set the normal mic button.
     */
    private void setNormalMic() {
        isListening = false;

        if (mCommandBoxSwitcher.getDisplayedChild() == 1) mCommandBoxSwitcher.showNext();

        mSendCommandBtn.setImageResource(R.drawable.ic_mic);
        mSendCommandBtn.setColorFilter(Color.rgb(255, 255, 255));
        mSendCommandBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                .getColor(this, R.color.primary)));
    }

    /**
     * Set the send text command button.
     */
    private void setSendBtn() {
        isListening = false;
        mSendCommandBtn.setImageResource(R.drawable.ic_send);
    }

    /**
     * Set the recording button.
     */
    private void setRecordingMic() {
        isListening = true;

        if (mCommandBoxSwitcher.getDisplayedChild() == 0) mCommandBoxSwitcher.showNext();

        mSendCommandBtn.setImageResource(R.drawable.ic_mic);
        mSendCommandBtn.setColorFilter(Color.rgb(29, 102, 96));
        mSendCommandBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat
                .getColor(this, android.R.color.holo_red_dark)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_AUDIO_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                } else {
                    Toast.makeText(this,
                            R.string.error_chat_room_record_permission_not_available,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Initialize the API.AI and TTS services. Set up the views.
     */
    private void init() {
        //Initialize API.AI Service.
        final AIConfiguration config = new AIConfiguration(BuildConfig.api_ai_access_key,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        mAiService = AIService.getService(this, config);
        mAiService.setListener(this);

        mCommandEt.addTextChangedListener(this);

        //Initialize the TTS
        TTS.init(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Release TTS.
        TTS.release();
    }

    /**
     * Whenever mic or send button clicks.
     */
    @OnClick(R.id.send_command_btn)
    void onSendCommand() {
        if (isProcessing) return;

        //If there is text in the command section, send it as text command.
        if (mCommandEt.getText().toString().trim().length() > 0) {

            mCommandTv.setText(mCommandEt.getText().toString().trim());

            //Create observer in Rx.
            final Observer<AIResponse> observer = new Observer<AIResponse>() {
                @Override
                public void onCompleted() {
                    mCommandEt.setText("");
                }

                @Override
                public void onError(Throwable e) {
                    ChatRoomActivity.this.onError(new AIError(e.getMessage()));
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
                        final AIRequest aiRequest = new AIRequest(mCommandEt.getText().toString().trim());
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

        } else if (!isListening) {

            //Start recording the speech.
            if (SpeechRecognizer.isRecognitionAvailable(this)) {
                mAiService.startListening();
            } else {
                Toast.makeText(this,
                        "Speech recognition isn't supported on this device.",
                        Toast.LENGTH_LONG).show();
            }
        } else {

            //Stop speech recognition.
            mAiService.stopListening();
        }
    }

    private void reset() {
        setNormalMic();
        mResponseTv.setText("");
        mCommandTv.setText("");
    }

    /**
     * On API AI query is resolved, this method will be called.
     *
     * @param aiResponse API AI response.
     */
    @Override
    public void onResult(AIResponse aiResponse) {
        isProcessing = false;
        final Result result = aiResponse.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        final String speech = result.getFulfillment().getSpeech();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCommandTv.setText(result.getResolvedQuery());
                mResponseTv.setText(speech);
            }
        });

        Log.d("Query:", speech + "\nAction: " + result.getAction() + "\nParameters: " + parameterString);
        TTS.speak(speech);
    }

    @Override
    public void onError(AIError aiError) {
        isProcessing = false;

        mCommandEt.setText("");
        setNormalMic();

        String speech = "Sorry, I did not understand what you were saying. Please say it again.";
        TTS.speak(speech);
        Log.d("Query:", "Error occurred " + aiError.getMessage());

        reset();
    }

    @Override
    public void onAudioLevel(float v) {
    }

    @Override
    public void onListeningStarted() {
        setRecordingMic();
    }

    @Override
    public void onListeningCanceled() {
        isProcessing = false;
        setNormalMic();
    }

    @Override
    public void onListeningFinished() {
        setNormalMic();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            setSendBtn();
        } else {
            setNormalMic();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
