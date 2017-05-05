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

package com.kevalpatel2106.home.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Keval on 20-Dec-16.
 * General utility functions.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class Utils {

    /**
     * Hide the soft keyboard.
     *
     * @param view view in focus.
     */
    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Check if the device has camera?
     *
     * @param context instance of caller.
     * @return true if the device has camera.
     */
    public static boolean checkIfHasCamera(@NonNull Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * Convert dp to pixel.
     *
     * @param context instance.
     * @param dp      value in dp
     * @return value in pixels.
     */
    public static float dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * Get the unique device id.
     *
     * @param context instance
     * @return device id.
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SuppressLint("PackageManagerGetSignatures")
    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retrieving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0)).replace("\n", "");
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static long convertToNano(long timeInMills) {
        return TimeUnit.MILLISECONDS.toNanos(timeInMills);
    }

    public static long convertToMilli(long timeInNano) {
        return TimeUnit.NANOSECONDS.toMillis(timeInNano);
    }

    public static String getFormatedDate(long timeInMills) {
        Calendar calendar = Calendar.getInstance();
        int todayDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        calendar.setTimeInMillis(timeInMills);
        if (todayDate == calendar.get(Calendar.DAY_OF_MONTH)) {
            return "Today";
        } else {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (calendar.get(Calendar.DAY_OF_MONTH) == todayDate) {
                return "Yesterday";
            } else {
                SimpleDateFormat sfd = new SimpleDateFormat("dd MMM", Locale.getDefault());
                return sfd.format(new Date(timeInMills));
            }
        }
    }

    /**
     * Set the maximum alarm for each audio streams.
     */
    public static void setMaxVolume(@NonNull Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, am.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        am.setStreamVolume(AudioManager.STREAM_RING, am.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, am.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
    }

    public static boolean isMicConnected(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }
}
