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

import android.widget.ListView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.*
import java.lang.Exception

class OverviewViewModel : ViewModel() {


    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _property = MutableLiveData<MarsProperty>()
    val property: LiveData<MarsProperty>
        get() = _property

    private val viewModelJob = Job()

    //use job to define scope
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


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

        /**                   RETROFIT WITH CALL IMPLEMENTATION                          */
//        MarsApi.retrofitService.getAllProperties().enqueue(object : retrofit2.Callback<List<MarsProperty>> {
//            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//                _response.value = "Success! ${response.body()?.size} Mars properties retrieved"
//            }
//
//            override fun onFailure(call: Call <List<MarsProperty>>, t: Throwable) {
//                _response.value = "failure " + t.message
//            }

        /**             RETROFIT INTEGRATION WITH COROUTINE AND DEFERRED IMPLEMENTATION [deprecated]           */
//        coroutineScope.launch {
//            val propertiesDeferred = MarsApi.retrofitService.getAllProperties()
//            try {
//                val propertiesList = propertiesDeferred.await()
//                _response.value = "Success! ${propertiesList.size} properties retrieved"
//            } catch (e: Exception) {
//                _response.value = "Failure ${e.message}"
//            }
//        }


        /**                 RETROFIT INTEGRATION WITH COROUTINE AND SUSPEND [latest]         */
        coroutineScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val propertiesList = MarsApi.retrofitService.getAllProperties()
                    if(propertiesList.isNotEmpty())
                        _property.postValue(propertiesList[0])
                    _status.value = "success! ${propertiesList.size} properties retrieved "
                }
            } catch (e: Exception) {
                _status.value = "Failure ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
