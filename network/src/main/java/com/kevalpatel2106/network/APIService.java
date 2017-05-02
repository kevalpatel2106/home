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

import com.kevalpatel2106.network.requestPojo.DeleteDeviceRequest;
import com.kevalpatel2106.network.requestPojo.DeviceRegisterRequest;
import com.kevalpatel2106.network.requestPojo.LoginRequest;
import com.kevalpatel2106.network.responsePojo.DeleteDeviceData;
import com.kevalpatel2106.network.responsePojo.DeviceListData;
import com.kevalpatel2106.network.responsePojo.DeviceRegisterData;
import com.kevalpatel2106.network.responsePojo.LoginResponseData;
import com.kevalpatel2106.network.responsePojo.base.BaseResponse;

import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Keval on 23-Apr-17.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

@SuppressWarnings("WeakerAccess")
public interface APIService {
    String BASE_URL = "http://jarvis-2106.appspot.com/";
    String ADMIN_BASE_URL = "http://admin.jarvis-2106.appspot.com/";

    //Login/Register apis
    @POST("updateGcmId")
    Observable<BaseResponse<LoginResponseData>> updateGcmId(@Body LoginRequest loginRequest);

    @POST("admin/registerDevice")
    Observable<BaseResponse<DeviceRegisterData>> registerDevice(@Header("Authorization") String authorization,
                                                                @Body DeviceRegisterRequest request);

    @POST("admin/deleteDevice")
    Observable<BaseResponse<DeleteDeviceData>> deleteDevice(@Header("Authorization") String authorization,
                                                            @Body DeleteDeviceRequest logoutRequest);

    @POST("admin/getAllDevices")
    Observable<BaseResponse<DeviceListData>> getAllDevicesAdmin(@Header("Authorization") String authorization);

    @POST("getAllDevicesAdmin")
    Observable<BaseResponse<DeviceListData>> getAllDevices(@Header("Authorization") String authorization);
}
