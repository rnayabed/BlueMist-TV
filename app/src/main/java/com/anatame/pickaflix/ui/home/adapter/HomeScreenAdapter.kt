package com.anatame.pickaflix.ui.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.databinding.ItemHomeViewpagerBinding
import com.anatame.pickaflix.utils.constants.Constants.COMING_SOON
import com.anatame.pickaflix.utils.constants.Constants.LATEST_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.NEW_TVSHOWS
import com.anatame.pickaflix.utils.constants.Constants.POPULAR_SHOWS
import com.anatame.pickaflix.utils.constants.Constants.TRENDING_MOVIES
import com.anatame.pickaflix.utils.constants.Constants.VIEW_PAGER

class HomeScreenAdapter(
    val context: Context
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
            0 -> {
                ViewPagerViewHolder(viewPagerItemBinding)
            }

            else -> {
                CategoryViewHolder(categoryItemBinding)
            }
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
        val heroList = listOf(
            HeroItem(
                backgroundImageUrl = "String",
                title = "String",
                source = "dgdg",
                duration = "String",
                rating = "String"
            ),
            HeroItem(
                backgroundImageUrl = "df",
                title = "String",
                source = "Strfdging",
                duration = "String",
                rating = "String"
            ),
            HeroItem(
                backgroundImageUrl = "Stringdf",
                title = "String",
                source = "dg",
                duration = "String",
                rating = "String"
            )
        )
        adapter.differ.submitList(heroList)
    }

    private fun setUpCategoryHolder(holder: CategoryViewHolder, viewType: Int) {
        val mBinding = holder.categoryItemBinding
        when(viewType){
            TRENDING_MOVIES -> setUpTrendingMovies(mBinding)
            POPULAR_SHOWS -> setUpPopularShows(mBinding)
            LATEST_MOVIES -> setUpLatestMovies(mBinding)
            NEW_TVSHOWS -> setUpNewTVShows(mBinding)
            COMING_SOON -> setUpComingSoon(mBinding)
        }
    }

    private fun setUpTrendingMovies(mBinding: ItemHomeCategoryBinding){
        mBinding.apply {
            val adapter = MovieAdapter(context)
            tvCategoryTitle.text = "Trending Movies"
            rvCategoryItems.adapter = adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.isNestedScrollingEnabled = false;
            val movieList = listOf(
                MovieItem(
                    title= "String",
                    thumbnailUrl= "String",
                    Url= "fhfh",
                    releaseDate= "String",
                    quality= "String",
                    length= "String",
                    movieType= "String",
                ),
                MovieItem(
                    title= "String",
                    thumbnailUrl= "String",
                    Url= "fhfhhfs",
                    releaseDate= "String",
                    quality= "String",
                    length= "String",
                    movieType= "String",
                ),
                MovieItem(
                    title= "fhfhfh",
                    thumbnailUrl= "String",
                    Url= "fsf",
                    releaseDate= "String",
                    quality= "String",
                    length= "String",
                    movieType= "String",
                ),
                MovieItem(
                    title= "String",
                    thumbnailUrl= "Stdggring",
                    Url= "sfStssrifnfg",
                    releaseDate= "String",
                    quality= "String",
                    length= "String",
                    movieType= "String",
                )
            )
            adapter.differ.submitList(movieList)
        }
    }

    private fun setUpPopularShows(mBinding: ItemHomeCategoryBinding){
        mBinding.apply {
            tvCategoryTitle.text = "Popular Shows"
        }
    }

    private fun setUpLatestMovies(mBinding: ItemHomeCategoryBinding){
        mBinding.apply {
            tvCategoryTitle.text = "Latest Movies"
        }
    }

    private fun setUpNewTVShows(mBinding: ItemHomeCategoryBinding){
        mBinding.apply {
            tvCategoryTitle.text = "New TV Shows"
        }
    }

    private fun setUpComingSoon(mBinding: ItemHomeCategoryBinding){
        mBinding.apply {
            tvCategoryTitle.text = "Coming Soon!"
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> VIEW_PAGER
            1 ->  TRENDING_MOVIES
            2 ->  POPULAR_SHOWS
            3 ->  LATEST_MOVIES
            4 ->  NEW_TVSHOWS
            else -> COMING_SOON
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

}


