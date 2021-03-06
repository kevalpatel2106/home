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
import com.kevalpatel2106.home.utils.Utils;

/**
 * Created by Keval on 27-Dec-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class LoginRequest {

    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("gcmKey")
    @Expose
    private String gcmKey;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;

    public LoginRequest(Context context) {
        this.deviceId = Utils.getDeviceId(context);
    }

    public void setGcmKey(String gcmKey) {
        this.gcmKey = gcmKey;
    }

    @Override
    public String toString() {
        return deviceId + " - " + gcmKey;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
