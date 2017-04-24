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

package com.kevalpatel2106.network.responsePojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Keval on 02-Jan-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

@SuppressWarnings("unused")
public class DeviceListData {
    @SerializedName("devices")
    @Expose
    private List<Device> devices = null;

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public class Device {

        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("gcmKey")
        @Expose
        private String gcmKey;
        @SerializedName("deviceName")
        @Expose
        private String deviceName;
        @SerializedName("deviceType")
        @Expose
        private String deviceType;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getGcmKey() {
            return gcmKey;
        }

        public void setGcmKey(String gcmKey) {
            this.gcmKey = gcmKey;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }
    }
}
