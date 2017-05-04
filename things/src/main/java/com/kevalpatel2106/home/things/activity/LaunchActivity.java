package com.kevalpatel2106.home.things.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kevalpatel2106.home.things.bluetooth.BluetoothControlService;
import com.kevalpatel2106.home.things.timeReminder.TimeReminderReceiver;
import com.kevalpatel2106.home.utils.cons.DeviceType;
import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;
import com.kevalpatel2106.home.utils.tts.TTS;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.requestPojo.LoginRequest;
import com.kevalpatel2106.network.responsePojo.LoginResponseData;

public class LaunchActivity extends AppCompatActivity {
    private static final String TAG = LaunchActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setMaxVolume();

        //register the device if already not registered
        FirebaseApp.initializeApp(this);
        if (!new DeviceSessionManager(this).isDeviceRegistered()) registerGcm();

        //start the bluetooth A2DP service.
        BluetoothControlService.turnOnBluetooth(this);

        //register for hourly reminder
        TimeReminderReceiver.registerReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Only release TTS here.
        TTS.release();
    }

    /**
     * Set the maximum alarm for each audio streams.
     */
    private void setMaxVolume() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
    }

    /**
     * Register and get the authentication token if the device is not connected already.
     */
    void registerGcm() {
        LoginRequest request = new LoginRequest(this);
        request.setDeviceType(DeviceType.PIE);
        request.setGcmKey(FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "Login request = " + request.toString());

        RetrofitUtils.subscribe(RetrofitUtils.getApiService().updateGcmId(request),
                new APIObserver<LoginResponseData>() {
                    @Override
                    public void onError(String errorMessage, int statusCode) {
                        Log.e(TAG, "GCM registration failed. -" + errorMessage);
                    }

                    @Override
                    public void onSuccess(LoginResponseData loginResponseData) {
                        new DeviceSessionManager(LaunchActivity.this)
                                .setNewSession(loginResponseData.getDeviceId(), loginResponseData.getToken());
                        Log.d(TAG, "GCM registered successfully.");
                    }
                });
    }
}
