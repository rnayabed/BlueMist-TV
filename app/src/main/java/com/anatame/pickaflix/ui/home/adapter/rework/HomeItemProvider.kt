package com.anatame.pickaflix.ui.home.adapter.rework

import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface HomeItemProvider  {
    fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun construct(position: Int, lifecycleOwner: LifecycleOwner)
}