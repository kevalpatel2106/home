package com.kevalpatel2106.home.things.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kevalpatel2106.home.utils.cons.DeviceType;
import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.requestPojo.LoginRequest;
import com.kevalpatel2106.network.responsePojo.LoginResponseData;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //register the device if already not registered
        if (!new DeviceSessionManager(this).isDeviceRegistered()) registerGcm();
    }

    void registerGcm() {
        FirebaseApp.initializeApp(this);

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
                        new DeviceSessionManager(MainActivity.this)
                                .setNewSession(loginResponseData.getDeviceId(), loginResponseData.getToken());

                        Log.d(TAG, "GCM registered successfully.");
                    }
                });
    }
}