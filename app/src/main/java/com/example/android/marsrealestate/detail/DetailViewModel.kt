/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.detail.DetailFragment
import com.example.android.marsrealestate.network.MarsProperty


/** WHY EXTEND ANDROID VIEW MODEL NOT VIEW MODEL? */
//requires selected mars property from overview viewmodel to display it
class DetailViewModel(@Suppress("UNUSED_PARAMETER")marsProperty: MarsProperty, app: Application) : AndroidViewModel(app) {

    //wrap property to live data to expose it to ui
    private val _selectedProperty = MutableLiveData<MarsProperty>()
    val selectedProperty : LiveData<MarsProperty>
        get() = _selectedProperty

    //formatted price live data string
    val selectedPropertyPrice = Transformations.map(selectedProperty){
        app.applicationContext.getString(when(it.isRental){
            true -> R.string.display_price_monthly_rental
            false -> R.string.display_price
        }, it.price)
    }

    val selectedPropertyType = Transformations.map(selectedProperty){
        app.applicationContext.getString(R.string.display_type, it.type)
    }

    //initialize live data to be displayed
    init {
        _selectedProperty.value = marsProperty
    }

}
