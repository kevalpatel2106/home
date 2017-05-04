/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kevalpatel2106.home.things.bluetooth;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kevalpatel2106.home.things.R;
import com.kevalpatel2106.home.utils.tts.TTS;

import java.util.Objects;

/**
 * Sample usage of the A2DP sink bluetooth profile. At startup, this activity sets the Bluetooth
 * adapter in pairing mode for {@link #DISCOVERABLE_TIMEOUT_SEC} ms.
 * <p>
 * NOTE: While in pairing mode, pairing requests are auto-accepted - at this moment there's no
 * way to block specific pairing attempts while in pairing mode. This is known limitation that is
 * being worked on.
 */
public class BluetoothControlService extends Service {
    private static final int STATE_TURN_ON = 1;
    private static final int STATE_TURN_OFF = 2;
    private static final int STATE_TURN_DISCONNECT_ALL = 3;
    private static final String ARG_BT_STATE = "bt_state";

    private static final String TAG = BluetoothControlService.class.getSimpleName();
    private static final String ADAPTER_FRIENDLY_NAME = "JarvisBT";

    private static final int FOREGROUND_NOTIFICATION_ID = 123;
    private static final int DISCOVERABLE_TIMEOUT_SEC = 300;

    /**
     * Handle an intent that is broadcast by the Bluetooth A2DP sink profile whenever a device
     * connects or disconnects to it.
     * Action is {@link A2dpSinkHelper#ACTION_CONNECTION_STATE_CHANGED} and
     * extras describe the old and the new connection states. You can use it to indicate that
     * there's a device connected.
     */
    private final BroadcastReceiver mSinkProfileStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(A2dpSinkHelper.ACTION_CONNECTION_STATE_CHANGED)) {
                BluetoothDevice device = A2dpSinkHelper.getDevice(intent);
                if (device != null) {

                    String deviceName = Objects.toString(device.getName(), "a device");
                    int newState = A2dpSinkHelper.getCurrentProfileState(intent);

                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        TTS.speak(BluetoothControlService.this, "Connected to " + deviceName);
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        TTS.speak(BluetoothControlService.this, "Disconnected from " + deviceName);
                    }
                }
            }
        }
    };

    /**
     * Handle an intent that is broadcast by the Bluetooth A2DP sink profile whenever a device
     * starts or stops playing through the A2DP sink.
     * Action is {@link A2dpSinkHelper#ACTION_PLAYING_STATE_CHANGED} and
     * extras describe the old and the new playback states. You can use it to indicate that
     * there's something playing. You don't need to handle the stream playback by yourself.
     */
    private final BroadcastReceiver mSinkProfilePlaybackChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(A2dpSinkHelper.ACTION_PLAYING_STATE_CHANGED)) {
                BluetoothDevice device = A2dpSinkHelper.getDevice(intent);
                if (device != null) {

                    int newState = A2dpSinkHelper.getCurrentProfileState(intent);
                    if (newState == A2dpSinkHelper.STATE_PLAYING) {
                        Log.i(TAG, "Playing audio from device " + device.getAddress());
                    } else if (newState == A2dpSinkHelper.STATE_NOT_PLAYING) {
                        Log.i(TAG, "Stopped playing audio from " + device.getAddress());
                    }
                }
            }
        }
    };

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothProfile mA2DPSinkProxy;

    /**
     * Handle an intent that is broadcast by the Bluetooth adapter whenever it changes its
     * state (after calling enable(), for example).
     * Action is {@link BluetoothAdapter#ACTION_STATE_CHANGED} and extras describe the old
     * and the new states. You can use this intent to indicate that the device is ready to go.
     */
    private final BroadcastReceiver mAdapterStateChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int newState = A2dpSinkHelper.getCurrentAdapterState(intent);
            if (newState == BluetoothAdapter.STATE_ON) {
                Log.i(TAG, "Bluetooth Adapter is ready");
                initA2DPSink();
            }
        }
    };

    public static void turnOnBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothControlService.class);
        intent.putExtra(BluetoothControlService.ARG_BT_STATE, STATE_TURN_ON);
        context.startService(intent);
    }

    public static void disconnectBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothControlService.class);
        intent.putExtra(BluetoothControlService.ARG_BT_STATE, STATE_TURN_DISCONNECT_ALL);
        context.startService(intent);
    }

    public static void turnOffBluetooth(Context context) {
        Intent intent = new Intent(context, BluetoothControlService.class);
        intent.putExtra(BluetoothControlService.ARG_BT_STATE, STATE_TURN_OFF);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.w(TAG, "No default Bluetooth adapter. Device likely does not support bluetooth.");
            return;
        }

        //Register receivers
        registerReceiver(mAdapterStateChangeReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(mSinkProfileStateChangeReceiver, new IntentFilter(A2dpSinkHelper.ACTION_CONNECTION_STATE_CHANGED));
        registerReceiver(mSinkProfilePlaybackChangeReceiver, new IntentFilter(A2dpSinkHelper.ACTION_PLAYING_STATE_CHANGED));

        if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth Adapter is already enabled.");
            initA2DPSink();
        } else {
            Log.d(TAG, "Bluetooth adapter not enabled. Enabling...");
            mBluetoothAdapter.enable();
        }

        makeForeground();
    }

    private void makeForeground() {
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Playing music")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getIntExtra(ARG_BT_STATE, -1)) {
            case STATE_TURN_ON:
                enableDiscoverable();
                break;
            case STATE_TURN_DISCONNECT_ALL:
                disconnectConnectedDevices();
                break;
            case STATE_TURN_OFF:
                stopForeground(true);
                stopSelf();
                break;
            default:
                stopSelf();
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        killService();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        killService();
    }

    /**
     * Release all the resources before killing the service.
     */
    private void killService() {
        //Unregister all the receiver.
        unregisterReceiver(mAdapterStateChangeReceiver);
        unregisterReceiver(mSinkProfileStateChangeReceiver);
        unregisterReceiver(mSinkProfilePlaybackChangeReceiver);

        if (mA2DPSinkProxy != null) {
            disconnectConnectedDevices();
            mBluetoothAdapter.closeProfileProxy(A2dpSinkHelper.A2DP_SINK_PROFILE, mA2DPSinkProxy);
            mBluetoothAdapter.disable();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Initiate the A2DP sink.
     */
    private void initA2DPSink() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Log.e(TAG, "Bluetooth adapter not available or not enabled.");
            return;
        }

        mBluetoothAdapter.setName(ADAPTER_FRIENDLY_NAME);   //Set the name of the bluetooth adapter
        mBluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {

            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                mA2DPSinkProxy = proxy;
                enableDiscoverable();
            }

            @Override
            public void onServiceDisconnected(int profile) {
            }
        }, A2dpSinkHelper.A2DP_SINK_PROFILE);
    }

    /**
     * Enable the current {@link BluetoothAdapter} to be discovered (available for pairing) for
     * the next {@link #DISCOVERABLE_TIMEOUT_SEC} ms.
     */
    private void enableDiscoverable() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIMEOUT_SEC);
        startActivity(discoverableIntent);
    }

    /**
     * Disconnect all the connected devices.
     */
    private void disconnectConnectedDevices() {
        if (mA2DPSinkProxy == null || mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            return;
        }
        for (BluetoothDevice device : mA2DPSinkProxy.getConnectedDevices()) {
            Log.i(TAG, "Disconnecting device " + device);
            A2dpSinkHelper.disconnect(mA2DPSinkProxy, device);
        }
    }
}