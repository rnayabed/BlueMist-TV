package com.anatame.pickaflix.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.model.scrollstate.HomeScrollStates
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.category.CategoryItem
import com.anatame.pickaflix.utils.constants.Constants.COMING_SOON
import com.anatame.pickaflix.utils.constants.Constants.LATEST_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.NEW_TVSHOWS
import com.anatame.pickaflix.utils.constants.Constants.POPULAR_SHOWS
import com.anatame.pickaflix.utils.constants.Constants.TRENDING_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.VIEW_PAGER
import com.anatame.pickaflix.utils.constants.Constants.WATCHLIST
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem

class HomeScreenAdapter(
    val context: Context,
    val homeFragment: HomeFragment,
    val homeScreenData: HomeScreenData,
    val hScrollStates: HomeScrollStates,
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
        holder.pagerBinding.vpHeroPager.adapter = adapter

        adapter.differ.submitList(homeScreenData.sliderItems)

        adapter.setOnItemClickListener{pos, heroItem, cardView ->
            homeFragment.navigateToDetailFromHero(cardView, holder, heroItem)
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


