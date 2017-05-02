package com.kevalpatel2106.home.things;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by Keval Patel on 23/04/17.
 *
 * @author 'https://github.com/kevalpatel2106'
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
