package com.kevalpatel2106.home.things.timeReminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.kevalpatel2106.home.utils.cons.Constants;
import com.kevalpatel2106.home.utils.tts.TTS;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Keval Patel on 04/05/17.
 * This receiver receives alarm for every hour.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class TimeReminderReceiver extends BroadcastReceiver {
    private static final String TAG = TimeReminderReceiver.class.getSimpleName();
    private static final int REA_CODE_PENDING_INTENT = 17263;

    /**
     * Register the alarm for the next hour.
     *
     * @param context instance of caller.
     */
    public static void registerReceiver(Context context) {

        //Get the milliseconds for next hour
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                REA_CODE_PENDING_INTENT,
                new Intent(context, TimeReminderReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Set the alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
        }

        Log.d(TAG, "registerReceiver: Next alarm registered.");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Time reminder receiver.");

        if (TTS.isIintilized()) {
            //Speak
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.setTimeZone(TimeZone.getTimeZone(Constants.TIME_ZONE));
            TTS.speak(context, "It\'s " + calendar.get(Calendar.HOUR) + " o clock");
        }

        //Register next alarm
        registerReceiver(context);
    }
}
