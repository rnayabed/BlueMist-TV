package com.anatame.pickaflix.ui.home.adapter.rework

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.google.android.material.transition.Hold
import kotlinx.coroutines.NonDisposableHandle.parent


class ViewPagerItem: HomeItemProvider {
    inner class Holder(
        pagerBinding: ItemHomeViewpagerBinding
    ): RecyclerView.ViewHolder(pagerBinding.root)

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return Holder(ItemHomeViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun <G> construct(context: Context, data: ArrayList<G>) {
        Log.d("fromViewPagerItem", data.toString())
    }


}