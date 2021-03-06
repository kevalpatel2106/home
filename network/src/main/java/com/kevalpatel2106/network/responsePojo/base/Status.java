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

package com.kevalpatel2106.network.responsePojo.base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Keval on 27-Dec-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */
@SuppressWarnings("unused")
public final class Status {
    @SerializedName("c")
    @Expose
    private Integer c;

    @SerializedName("m")
    @Expose
    private String m;

    public Integer getStatusCode() {
        return c;
    }

    public void setC(Integer c) {
        this.c = c;
    }

    public String getMessage() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

}
