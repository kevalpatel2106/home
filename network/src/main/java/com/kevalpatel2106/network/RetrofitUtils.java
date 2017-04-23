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

package com.kevalpatel2106.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.kevalpatel2106.home.utils.managers.DeviceSessionManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Keval on 20-Dec-16.
 * Build the client for api service.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public final class RetrofitUtils {

    /**
     * Get the instance of the retrofit {@link APIService}.
     *
     * @return {@link APIService}
     */
    public static APIService getApiService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        //Enable logging for debug build
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);

        //Building retrofit
        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(APIService.BASE_URL)
                .client(client.build())
                .build();

        return retrofit.create(APIService.class);
    }

    /**
     * Get the instance of the retrofit {@link APIService}.
     *
     * @return {@link APIService}
     */
    public static APIService getAdminApiService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();

        //Enable logging for debug build
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);

        //Building retrofit
        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(APIService.ADMIN_BASE_URL)
                .client(client.build())
                .build();

        return retrofit.create(APIService.class);
    }

    /**
     * Subscribe to the observer and start API call.
     *
     * @param observable  observable
     * @param APIObserver response listener {@link APIObserver} class.
     * @return new {@link Subscription} created.
     */
    @SuppressWarnings("unchecked")
    public static Subscription subscribe(@NonNull Observable observable,
                                         @NonNull APIObserver APIObserver) {
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(APIObserver);
    }

    /**
     * Get the authentication header string.
     *
     * @param context instance of the activity/fragment
     * @return (UserId : Token) base64 encoded string
     */
    public static String getAuthString(Context context) {
        DeviceSessionManager userSessionManager = new DeviceSessionManager(context);
        return "Basic " + Base64.encodeToString((userSessionManager.getDeviceId() + ":" + userSessionManager.getToken()).getBytes(),
                Base64.NO_WRAP);
    }
}
