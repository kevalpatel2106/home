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

package com.kevalpatel2106.home.utils.rx;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Keval on 20-Dec-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class RxUtils {

    /**
     * Subscribe observer on the main thread and observable on new worker thread.
     */
    public static Subscription startObservable(@NonNull Observable.OnSubscribe observable,
                                               @NonNull Action1 action1) {
        //noinspection unchecked
        return Observable.create(observable)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
    }

    /**
     * Subscribe observer on the main thread and observable on new worker thread.
     */
    public static Subscription startObservable(@NonNull Observable.OnSubscribe observable,
                                               @NonNull Observer observer) {
        //noinspection unchecked
        return Observable.create(observable)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
