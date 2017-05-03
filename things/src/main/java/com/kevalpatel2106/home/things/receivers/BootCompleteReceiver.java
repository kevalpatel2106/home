package com.kevalpatel2106.home.things.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kevalpatel2106.home.things.activity.LaunchActivity;

/**
 * Created by Keval on 02-May-17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent();
        i.setClassName(LaunchActivity.class.getPackage().getName(), LaunchActivity.class.getSimpleName());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
