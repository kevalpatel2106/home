package com.kevalpatel2106.home.things.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kevalpatel2106.ftp_server.FTPManager;
import com.kevalpatel2106.home.things.bluetooth.BluetoothA2DPService;
import com.kevalpatel2106.home.things.speechToText.SpeechRecognitionService;
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

    private boolean isBound = false;        //Bool indicating if the services are bound?
    private SpeechRecognitionService mService;  //Object of the SpeechRecognitionService
    private FTPManager mFTPManager;

    /**
     * Service connection listener for {@link SpeechRecognitionService}.
     */
    private ServiceConnection mSpeechRecognitionServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SpeechRecognitionService.LocalBinder binder = (SpeechRecognitionService.LocalBinder) service;
            mService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize TTS so the tts responses don't take too much time.
        TTS.speak(this, "I am ready.");

        //register the device if already not registered
        FirebaseApp.initializeApp(this);
        if (!new DeviceSessionManager(this).isDeviceRegistered()) registerGcm();

        //start the bluetooth A2DP service.
        BluetoothA2DPService.turnOnBluetooth(this);

        //register for hourly reminder
        TimeReminderReceiver.registerReceiver(this);

        //start ftp server
        mFTPManager = new FTPManager(this);
        mFTPManager.startServer(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to SpeechRecognitionService
        Intent intent = new Intent(this, SpeechRecognitionService.class);
        bindService(intent, mSpeechRecognitionServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (isBound) {
            unbindService(mSpeechRecognitionServiceConnection);
            isBound = false;
        }

        mFTPManager.stopServer();
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
