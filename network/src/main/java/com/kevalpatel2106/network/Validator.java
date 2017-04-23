package com.kevalpatel2106.network;

/**
 * Created by Keval Patel on 23/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class Validator {
    public static final int DEVICE_NAME_MIN_LENGTH = 4;
    public static final int DEVICE_ID_MIN_LENGTH = 4;

    public static boolean isValidDeviceName(String name) {
        return !name.isEmpty() || name.length() < DEVICE_NAME_MIN_LENGTH;
    }

    public static boolean isValidDeviceId(String deviceId) {
        return !deviceId.isEmpty() || deviceId.length() < DEVICE_ID_MIN_LENGTH;
    }
}
