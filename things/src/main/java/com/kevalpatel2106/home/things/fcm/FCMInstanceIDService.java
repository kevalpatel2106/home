/*
 * Copyright 2017 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel2106.home.things.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;
import com.kevalpatel2106.network.APIObserver;
import com.kevalpatel2106.network.RetrofitUtils;
import com.kevalpatel2106.network.requestPojo.LoginRequest;
import com.kevalpatel2106.network.responsePojo.LoginResponseData;

/**
 * Created by Keval on 08-Feb-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
public class FCMInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = FCMInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        registerDevice();
    }

    void registerDevice() {
        LoginRequest request = new LoginRequest(this);
        request.setGcmKey(FirebaseInstanceId.getInstance().getToken());
        Log.d(TAG, "Login request =" + request.toString());

        RetrofitUtils.subscribe(RetrofitUtils.getApiService().updateGcmId(request),
                new APIObserver<LoginResponseData>() {
                    @Override
                    public void onError(String errorMessage, int statusCode) {
                        Log.e(TAG, "GCM registration failed. -" + errorMessage);
                    }

                    @Override
                    public void onSuccess(LoginResponseData loginResponseData) {
                        new DeviceSessionManager(FCMInstanceIDService.this)
                                .setNewSession(loginResponseData.getDeviceId(), loginResponseData.getToken());

                        Log.d(TAG, "GCM registered successfully.");
                    }
                });
    }
}
