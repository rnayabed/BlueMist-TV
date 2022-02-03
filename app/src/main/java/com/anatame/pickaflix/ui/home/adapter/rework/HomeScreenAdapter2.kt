package com.anatame.pickaflix.ui.home.adapter.rework

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.ui.home.HomeFragment

class HomeScreenAdapter2(
    val context: Context,
    val repo: HomeItemRepo,
    val homeFragment: HomeFragment,
    val lifecycleOwner: LifecycleOwner,
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("oncreateCalled", viewType.toString())
        return repo.getItem(viewType).getViewHolder(
            context,
            parent,
            lifecycleOwner,
            homeFragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("onBindIsCalled", position.toString())
        repo.getItem(position).onBindHolder(context, position, lifecycleOwner)
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return repo.getItemSize()
    }

    fun showContinueWatchingCard() {

    }

}


