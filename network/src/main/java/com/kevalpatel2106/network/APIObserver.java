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


import com.kevalpatel2106.network.responsePojo.BaseResponse;

import rx.Observer;

/**
 * Created by Keval on 23-Apr-17.
 * This is an abstract class that will process the response received and classify them into two methods:
 * <li>onError - Indicates error</li>
 * <li>onSuccess - Indicates response is successful/li>
 * <p>
 * T must be inherited from {@link BaseResponse}
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public abstract class APIObserver<T> implements Observer<BaseResponse> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onError(e.getMessage(), -1000);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onNext(BaseResponse baseResponse) {
        int statusCode = baseResponse.getStatus().getStatusCode();

        if (statusCode == APIStatusCodes.SUCCESS_CODE) {
            onSuccess((T) baseResponse.getData());
        } else if (statusCode == APIStatusCodes.ERROR_CODE_UNAUTHORIZED) {
            //TODO Handle unauthorized
            onError(baseResponse.getStatus().getMessage(), statusCode);
        } else if (statusCode > APIStatusCodes.SUCCESS_CODE) {
            onError(baseResponse.getStatus().getMessage(), statusCode);
        } else {
            onError("Something went wrong.", statusCode);
        }
    }

    /**
     * This method indicates that there is an error response from the server.
     *
     * @param errorMessage Message to display to user
     * @param statusCode   error code. If there is HTTP error it will pass HTTP error code.
     */
    public abstract void onError(String errorMessage, int statusCode);

    /**
     * This indicates success response.
     *
     * @param baseResponseData Response.
     */
    public abstract void onSuccess(T baseResponseData);
}