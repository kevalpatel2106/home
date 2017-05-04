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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kevalpatel2106.home.things.apiai.ApiAiManager;

/**
 * Created by Keval on 08-Feb-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TYPE_SEND_COMMAND_TO_AT = "sendCommandAt";

    private static final String TAG = FCMMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().toString());

        String ct = remoteMessage.getData().get("ct");
        if (ct == null || ct.isEmpty()) return;

        switch (ct) {
            case TYPE_SEND_COMMAND_TO_AT:
                android.os.Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ApiAiManager apiAiManager = new ApiAiManager(FCMMessagingService.this);
                        apiAiManager.send(remoteMessage.getData().get("commandText"));
                    }
                });
                break;
        }
    }
}
