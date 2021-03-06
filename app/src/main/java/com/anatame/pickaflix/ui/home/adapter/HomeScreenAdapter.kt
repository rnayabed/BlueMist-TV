package com.anatame.pickaflix.ui.home.adapter

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
import com.anatame.pickaflix.ui.home.adapter.rework.childAdapters.HeroPagerAdapter
import com.anatame.pickaflix.ui.home.category.CategoryItem
import com.anatame.pickaflix.utils.constants.Constants.COMING_SOON
import com.anatame.pickaflix.utils.constants.Constants.LATEST_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.NEW_TVSHOWS
import com.anatame.pickaflix.utils.constants.Constants.POPULAR_SHOWS
import com.anatame.pickaflix.utils.constants.Constants.TRENDING_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.VIEW_PAGER
import com.anatame.pickaflix.utils.constants.Constants.WATCHLIST
import java.lang.Math.abs

class HomeScreenAdapter(
    val context: Context,
    val homeFragment: HomeFragment,
    val homeScreenData: HomeScreenData,
    val hScrollStates: HomeScrollStates,
    val heroScrollState: MutableLiveData<Int>,
    val lifecycleOwner: LifecycleOwner
):  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewPagerViewHolder(
        val pagerBinding: ItemHomeViewpagerBinding
    ):  RecyclerView.ViewHolder(pagerBinding.root)

    inner class CategoryViewHolder(
        val categoryItemBinding: ItemHomeCategoryBinding
    ): RecyclerView.ViewHolder(categoryItemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewPagerItemBinding = ItemHomeViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val categoryItemBinding = ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return when(viewType){
            0 -> ViewPagerViewHolder(viewPagerItemBinding)
            else -> CategoryViewHolder(categoryItemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0 -> setUpViewPagerHolder(holder as ViewPagerViewHolder)
            else -> setUpCategoryHolder(holder as CategoryViewHolder, holder.itemViewType)
        }
    }

    private fun setUpViewPagerHolder(holder: ViewPagerViewHolder){
        val adapter = HeroPagerAdapter(context)
        val viewpager = holder.pagerBinding.vpHeroPager

        viewpager.setPageTransformer(ZoomOutPageTransformer())
        viewpager.adapter = adapter
        viewpager.setCurrentItem(3, false)

        viewpager.offscreenPageLimit = 1

// Add a PageTransformer that translates the next and previous items horizontally
// towards the center of the screen, which makes them visible
        val nextItemVisiblePx = context.resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = context.resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        viewpager.setPageTransformer(pageTransformer)

// The ItemDecoration gives the current (centered) item horizontal margin so that
// it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            context,
            R.dimen.viewpager_current_item_horizontal_margin
        )
        viewpager.addItemDecoration(itemDecoration)

        adapter.differ.submitList(homeScreenData.sliderItems)

        heroScrollState.observe(lifecycleOwner, Observer{
            holder.pagerBinding.vpHeroPager.setCurrentItem(it, false)
            Log.d("viewpagerstate", "bruh")
        })

        adapter.setOnItemClickListener{pos, heroItem, cardView ->
            Handler(Looper.getMainLooper()).postDelayed({
                heroScrollState.postValue(pos)
            }, 300)
            homeFragment.navigateToDetailFromHero(cardView, holder, heroItem, pos)
        }
    }

    private fun setUpCategoryHolder(holder: CategoryViewHolder, viewType: Int) {
        val mBinding = holder.categoryItemBinding
        val categoryItem = CategoryItem(mBinding, context, homeFragment, holder, homeScreenData, hScrollStates, lifecycleOwner)
        when(viewType){
            WATCHLIST -> {
                categoryItem.setUpWatchList()
            }
            TRENDING_MOVIES -> categoryItem.setUpTrendingMovies()
            POPULAR_SHOWS -> categoryItem.setUpPopularShows()
            LATEST_MOVIES -> categoryItem.setUpLatestMovies()
            NEW_TVSHOWS -> categoryItem.setUpNewTVShows()
            COMING_SOON -> categoryItem.setUpComingSoon()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_PAGER
            1 ->  WATCHLIST
            2 ->  TRENDING_MOVIES
            3 ->  POPULAR_SHOWS
            4 ->  LATEST_MOVIES
            5 -> NEW_TVSHOWS
            else -> COMING_SOON
        }
    }

    override fun getItemCount(): Int {
        return 7
    }

    fun showContinueWatchingCard() {

    }

}


