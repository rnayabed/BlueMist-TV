package com.anatame.pickaflix.ui.home.adapter.rework

import com.anatame.pickaflix.ui.home.adapter.rework.providers.HomeItemProvider

class HomeItemRepo {
    private val items: ArrayList<HomeItemProvider> = ArrayList()

    fun addItem(item: HomeItemProvider){
        items.add(item)
    }

    fun getItem(p: Int): HomeItemProvider {
        return items[p]
    }

    fun getItemSize(): Int{
        return items.size
    }
}