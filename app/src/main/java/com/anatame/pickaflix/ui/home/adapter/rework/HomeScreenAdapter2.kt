package com.anatame.pickaflix.ui.home.adapter.rework

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.model.scrollstate.HomeScrollStates
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.ViewPager.HorizontalMarginItemDecoration
import com.anatame.pickaflix.ui.home.ViewPager.ZoomOutPageTransformer
import com.anatame.pickaflix.ui.home.adapter.childAdapters.HeroPagerAdapter
import com.anatame.pickaflix.ui.home.category.CategoryItem
import com.anatame.pickaflix.utils.constants.Constants.COMING_SOON
import com.anatame.pickaflix.utils.constants.Constants.LATEST_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.NEW_TVSHOWS
import com.anatame.pickaflix.utils.constants.Constants.POPULAR_SHOWS
import com.anatame.pickaflix.utils.constants.Constants.TRENDING_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.VIEW_PAGER
import com.anatame.pickaflix.utils.constants.Constants.WATCHLIST
import java.lang.Math.abs

class HomeScreenAdapter2(
    val context: Context,
    val repo: HomeItemRepo,
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("oncreateCalled", viewType.toString())
        return repo.getItem(viewType).getViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

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


