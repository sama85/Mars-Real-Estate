/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class OverviewViewModel : ViewModel() {


    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    //view model initialization to display data via initializing live data
    init {
        getMarsRealEstateProperties()
    }

    //initializes live data of UI
    private fun getMarsRealEstateProperties() {
        /*
            1. all retrofit requests are wrapped into a call object, each yields its own http request and response
            2. call interface provides 2 method to make http request:
                execute() -> synchronous, network http request runs on main thread (UI blocking)
                enqueue(callback<T>) -> asynchronous, network http request runs on a background thread,
                                        UI non-blocking, runs call back methods (response, failure) in main thread
        */
        MarsApi.retrofitService.getAllProperties().enqueue(object : retrofit2.Callback<List<MarsProperty>> {
            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
                _response.value = "Success! ${response.body()?.size} Mars properties retrieved"
            }

            override fun onFailure(call: Call <List<MarsProperty>>, t: Throwable) {
                _response.value = "failure " + t.message
            }

        })
    }
}
