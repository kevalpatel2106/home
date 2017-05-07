package com.kevalpatel2106.home.utils;

import android.content.Context;
import android.media.AudioManager;
import android.support.annotation.NonNull;

import com.kevalpatel2106.home.utils.managers.SharedPrefsManager;

/**
 * Created by Keval Patel on 07/05/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class AudioUtils {
    private static final String PREF_KEY_VOLUME = "presetVolume";


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

    public static void increaseVolumeBy(Context context, int changeValue) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC) + changeValue;
        if (volume > am.getStreamMaxVolume(AudioManager.STREAM_MUSIC))
            volume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
    }

    public static void decreaseVolumeBy(Context context, int changeValue) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int volume = am.getStreamVolume(AudioManager.STREAM_MUSIC) - changeValue;
        if (volume < 0) volume = 0;

        am.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_RING, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, volume, 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, 0);
    }

    public static void setVolumeTo(Context context, int finalValue) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, finalValue, 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, finalValue, 0);
        am.setStreamVolume(AudioManager.STREAM_RING, finalValue, 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, finalValue, 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, finalValue, 0);
    }

    public static void muteVolume(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        new SharedPrefsManager(context).savePreferences(PREF_KEY_VOLUME,
                am.getStreamVolume(AudioManager.STREAM_MUSIC));

        am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
        am.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, 0, 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, 0, 0);
    }

    public static void unmuteVolume(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int presetVolume = new SharedPrefsManager(context).getIntFromPreference(PREF_KEY_VOLUME,
                am.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

        am.setStreamVolume(AudioManager.STREAM_MUSIC, presetVolume, 0);
        am.setStreamVolume(AudioManager.STREAM_ALARM, presetVolume, 0);
        am.setStreamVolume(AudioManager.STREAM_RING, presetVolume, 0);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, presetVolume, 0);
        am.setStreamVolume(AudioManager.STREAM_SYSTEM, presetVolume, 0);
    }

    public static int getCurrentVolume(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getStreamVolume(AudioManager.STREAM_MUSIC);
    }
}
