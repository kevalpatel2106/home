package com.kevalpatel2106.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kevalpatel2106.home.base.BaseActivity;
import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;

/**
 * Created by Keval Patel on 23/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new DeviceSessionManager(this).isDeviceRegistered() ?
                new Intent(this, ChatRoomActivity.class) :
                new Intent(this, LoginActivity.class));
    }
}
