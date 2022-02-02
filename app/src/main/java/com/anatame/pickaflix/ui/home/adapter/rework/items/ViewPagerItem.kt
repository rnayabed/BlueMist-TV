package com.anatame.pickaflix.ui.home.adapter.rework.items

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.ui.home.adapter.rework.HomeItemProvider
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem


class ViewPagerItem(
    val context: Context,
    val data: List<HeroItem>
): HomeItemProvider {
    inner class Holder(
        pagerBinding: ItemHomeViewpagerBinding
    ): RecyclerView.ViewHolder(pagerBinding.root)

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return Holder(ItemHomeViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun construct(position: Int, lifecycleOwner: LifecycleOwner) {
        Log.d("fromViewPagerItem", data.toString())
    }


}