package com.anatame.pickaflix.ui.home.adapter.rework

import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.utils.Resource

class HomeItemRepo {
    private val items: ArrayList<HomeItemProvider> = ArrayList()

    fun addItem(item: HomeItemProvider){
        items.add(item)
    }

    fun getItem(p: Int): HomeItemProvider{
        return items[p]
    }

    fun getItemSize(): Int{
        return items.size
    }
}