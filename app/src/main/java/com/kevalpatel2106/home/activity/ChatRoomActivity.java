package com.kevalpatel2106.home.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.kevalpatel2106.home.BuildConfig;
import com.kevalpatel2106.home.R;
import com.kevalpatel2106.home.base.BaseActivity;

import java.util.Map;

import ai.api.AIListener;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Result;
import butterknife.BindView;
import butterknife.OnClick;

public class ChatRoomActivity extends BaseActivity implements AIListener {
    private static final int REQ_CODE_AUDIO_PERMISSION = 7872;
    @BindView(R.id.mic_button)
    FloatingActionButton mMicBtn;
    private boolean isListening = false;
    private boolean isProcessing = false;
    private AIService mAiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        setNormalMic();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            initApiAiService();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQ_CODE_AUDIO_PERMISSION);
        }
    }

    private void setNormalMic() {
        isListening = false;

        mMicBtn.setColorFilter(Color.rgb(255, 255, 255));
        mMicBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
    }

    private void setRecordingMic() {
        isListening = true;

        mMicBtn.setColorFilter(Color.rgb(29, 102, 96));
        mMicBtn.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_CODE_AUDIO_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initApiAiService();
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

    private void initApiAiService() {
        final AIConfiguration config = new AIConfiguration(BuildConfig.api_ai_access_key,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        mAiService = AIService.getService(this, config);
        mAiService.setListener(this);
    }

    @OnClick(R.id.mic_button)
    void onMicClick() {
        if (isProcessing) return;

        if (!isListening) {
            Log.d("APIAI", "listen command fired");
            mAiService.startListening();
        } else {
            Log.d("APIAI", "stop listen command fired");
            mAiService.stopListening();
        }
    }

    @Override
    public void onResult(AIResponse aiResponse) {
        isProcessing = false;

        Result result = aiResponse.getResult();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        Log.d("Query:", result.getResolvedQuery() + "\nAction: " + result.getAction() + "\nParameters: " + parameterString);
    }

    @Override
    public void onError(AIError aiError) {
        isProcessing = false;
        Log.d("Query:", "Error occurred " + aiError.getMessage());
    }

    @Override
    public void onAudioLevel(float v) {

    }

    @Override
    public void onListeningStarted() {
        Log.d("APIAI", "listen started");
        setRecordingMic();
    }

    @Override
    public void onListeningCanceled() {
        Log.d("APIAI", "listen canceled");
        isProcessing = false;
        setNormalMic();
    }

    @Override
    public void onListeningFinished() {
        Log.d("APIAI", "listen finished");
        setNormalMic();
    }
}
