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

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Keval on 17-Dec-16.
 * Rx bus, a replacement of the event bus.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class RxBus {

    private static final RxBus INSTANCE = new RxBus();

    private final Subject<Object, Object> mBusSubject = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {
    }

    public static RxBus getInstance() {
        return INSTANCE;
    }

    public void send(Object o) {
        mBusSubject.onNext(o);
    }

    /**
     * Register new event listener.
     *
     * @param eventClass event class.
     * @param action1    event receiver
     * @return new subscription.
     */
    @SuppressWarnings("unchecked")
    public <T> Subscription register(final Class<T> eventClass, Action1<Object> action1) {
        return mBusSubject
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return o.getClass().equals(eventClass);
                    }
                })
                .map(new Func1<Object, Object>() {
                    @Override
                    public T call(Object o) {
                        return (T) o;
                    }
                })
                .subscribe(action1);
    }
}
