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

package com.kevalpatel2106.home.utils.managers;

import android.content.Context;
import android.support.annotation.NonNull;


/**
 * Created by Keval on 24-Oct-16.
 * This class manages user login session.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public final class DeviceSessionManager {

    //User preference keys.
    private static final String DEVICE_ID = "DEVICE_ID";                    //id of the device
    private static final String USER_TOKEN = "USER_TOKEN";                  //Authentication token

    private SharedPrefsManager mSharedPrefsManager;

    /**
     * Public constructor.
     *
     * @param context instance of the caller.
     */
    public DeviceSessionManager(@NonNull Context context) {
        mSharedPrefsManager = new SharedPrefsManager(context);
    }

    /**
     * Set user session detail.
     *
     * @param deviceId unique id of the user
     * @param token    authentication token
     */
    public void setNewSession(@NonNull String deviceId,
                              @NonNull String token) {
        //Save to the share prefs.
        mSharedPrefsManager.savePreferences(DEVICE_ID, deviceId);
        mSharedPrefsManager.savePreferences(USER_TOKEN, token);
    }

    /**
     * First name of the user.
     *
     * @return first name
     */
    public String getDeviceId() {
        return mSharedPrefsManager.getStringFromPreferences(DEVICE_ID);
    }

    /**
     * Get current authentication token
     *
     * @return token
     */
    public String getToken() {
        return mSharedPrefsManager.getStringFromPreferences(USER_TOKEN);
    }

    /**
     * Set current authentication token
     */
    public void setToken(String token) {
        mSharedPrefsManager.savePreferences(USER_TOKEN, token);
    }

    /**
     * Clear token from the session.
     */
    public void clearToken() {
        mSharedPrefsManager.removePreferences(USER_TOKEN);
    }

    /**
     * Clear user data.
     */
    public void clearUserSession() {
        mSharedPrefsManager.removePreferences(DEVICE_ID);
        mSharedPrefsManager.removePreferences(USER_TOKEN);
    }

    /**
     * Check if the user is currently logged in?
     *
     * @return true if the user is currently logged in.
     */
    public boolean isDeviceRegistered() {
        return mSharedPrefsManager.getStringFromPreferences(DEVICE_ID) != null
                && mSharedPrefsManager.getStringFromPreferences(USER_TOKEN) != null;
    }
}
