package com.anatame.pickaflix.ui.home.adapter.rework

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface HomeItemProvider  {
    fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun <G> construct(context: Context, data: ArrayList<G>)
}