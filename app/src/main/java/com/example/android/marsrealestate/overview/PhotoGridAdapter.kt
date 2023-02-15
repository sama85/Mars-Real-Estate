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

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.android.marsrealestate.R
import com.example.android.marsrealestate.databinding.GridViewItemBinding
import com.example.android.marsrealestate.network.MarsProperty

class PhotoGridAdapter(val propertyListener: MarsPropertyListener) :
    androidx.recyclerview.widget.ListAdapter<MarsProperty, PhotoGridAdapter.MarsPropertyViewHolder>(
        diffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoGridAdapter.MarsPropertyViewHolder {
        return MarsPropertyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.MarsPropertyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, propertyListener)
    }


    //View holder with view binding
    class MarsPropertyViewHolder(val binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MarsPropertyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = GridViewItemBinding.inflate(inflater)
                return MarsPropertyViewHolder(binding)
            }
        }

        fun bind(property: MarsProperty, propertyListener: MarsPropertyListener) {
            val imageUrl = property.imageSrcUrl
            binding.root.setOnClickListener{
                propertyListener.onClick(property)
            }
            binding.marsImage.bind(imageUrl)
        }
    }

}

object diffCallback : DiffUtil.ItemCallback<MarsProperty>() {
    override fun areItemsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MarsProperty, newItem: MarsProperty): Boolean {
        return oldItem == newItem
    }
}

interface MarsPropertyListener {
    fun onClick(property: MarsProperty)
}


fun ImageView.bind(imageUrl: String) {

    imageUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(this.context)
            .load(imgUri)
            //request place holder image from glide to be displayed while img is loading
            //and error image when it can't be retrieved
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(this)
    }
}

