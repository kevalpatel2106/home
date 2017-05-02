package com.kevalpatel2106.home.things.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kevalpatel2106.home.things.activity.MainActivity;

/**
 * Created by Keval on 02-May-17.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Open the application
        context.startActivity(new Intent(context, MainActivity.class));
    }
}
