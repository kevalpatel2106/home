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

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kevalpatel2106.home.things.activity.A2DPSinkActivity;

/**
 * Created by Keval on 08-Feb-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
public class FCMMessagingService extends FirebaseMessagingService {
    private static final String NOTIFICATION_TYPE_A2DP = "init.a2dp";

    private static final String TAG = FCMMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().toString());

        String ct = remoteMessage.getData().get("ct");
        if (ct == null || ct.isEmpty()) return;

        switch (ct) {
            case NOTIFICATION_TYPE_A2DP:
                Intent intent = new Intent(this, A2DPSinkActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Log.d(TAG, "onMessageReceived: Opening A2DP activity");
                break;
        }
    }
}
