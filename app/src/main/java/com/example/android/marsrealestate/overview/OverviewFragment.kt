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

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.FragmentOverviewBinding
import com.example.android.marsrealestate.network.MarsProperty

class OverviewFragment : Fragment() {

    //similar to lateinit but includes init logic and initializes object when first called/accessed
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Data binding binds live data to views and observes the data and updates UI/views accordingly
        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        // and updates UI accordingly
        binding.lifecycleOwner = this

        // Giving the binding access to the OverviewViewModel which holds ui data
        binding.viewModel = viewModel

        //Recycler view is empty when data (properties) is still being fetched from server
        //image view will take place until data (properties) is fetched
        //then VH will be created and bind method will use glide to load images from urls in objects
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when(it){
                MarsApiStatus.ERROR -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.ic_connection_error)
                }
                MarsApiStatus.LOADING -> {
                    binding.statusImage.visibility = View.VISIBLE
                    binding.statusImage.setImageResource(R.drawable.loading_animation)
                }
                else -> binding.statusImage.visibility = View.GONE
            }
        })

        val propertyListener = object : MarsPropertyListener{
            override fun onClick(property: MarsProperty) {
                viewModel.navigateToPropertyDetail(property)
            }
        }

        val adapter = PhotoGridAdapter(propertyListener)
        binding.photosGrid.adapter = adapter

        viewModel.properties.observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
        })

        viewModel.navigateToPropertyDetail.observe(viewLifecycleOwner, Observer{
            it?.let {
                //navigate with selected object
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
            }
        })


        setHasOptionsMenu(true)
        return binding.root
    }

    //Inflates the overflow menu that contains filtering options.
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
