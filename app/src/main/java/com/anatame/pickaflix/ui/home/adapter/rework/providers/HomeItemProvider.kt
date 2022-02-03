package com.anatame.pickaflix.ui.home.adapter.rework.providers

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface HomeItemProvider  {
    fun getViewHolder(context: Context, parent: ViewGroup, lifecycleOwner: LifecycleOwner): RecyclerView.ViewHolder
    fun onBindHolder(context: Context, position: Int, lifecycleOwner: LifecycleOwner)
}