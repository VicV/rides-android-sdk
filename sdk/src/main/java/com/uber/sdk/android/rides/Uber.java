package com.uber.sdk.android.rides;

/*
 * Copyright (c) 2015 Uber Technologies, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * Created by VicV on 1/29/2016.
 */

import com.uber.sdk.android.rides.model.UserActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class Uber {

    private static Uber instance;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    public static Uber getInstance() {
        if (instance == null) {
            instance = new Uber();
        }
        return instance;
    }

    public UserActivity getRideHistory(int offset, int limit) {
        new Retrofit.Builder().baseUrl("https://login.uber.com/").addConverterFactory(GsonConverterFactory.create())
                .build().create(UserActivityInterface.class).getUserActiviy(accessToken, offset, limit).enqueue(
                new Callback<UserActivity>() {
                    @Override
                    public void onResponse(Response<UserActivity> response) {
                        //Make async response
                        return response.body();
                    }

                    @Override
                    public void onFailure(Throwable t) {


                    }
                });
    }


    private interface UserActivityInterface {

        @GET("/history/")
        Call<UserActivity> getUserActiviy(@Header("Authorization") String auth,
                                          @Query("offset") int ofset,
                                          @Query("limit") int limit);
    }

}
