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

package com.kevalpatel2106.network.requestPojo;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kevalpatel.hourlogger.data.managers.UserSessionManager;
import com.kevalpatel.hourlogger.utils.Utils;

/**
 * Created by Keval on 27-Dec-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
@SuppressWarnings("FieldCanBeLocal")
public class DeleteDeviceRequest {

    @SerializedName("uid")
    @Expose
    private long userId;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    public DeleteDeviceRequest(Context context) {
        this.deviceId = Utils.getDeviceId(context);
        this.userId = new UserSessionManager(context).getUserId();
    }
}
