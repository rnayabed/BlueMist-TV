package com.anatame.pickaflix.ui.home.category

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.home.adapter.MovieAdapter
import com.anatame.pickaflix.ui.home.adapter.WatchListAdapter
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieDetails

class CategoryItem(
    private val mBinding: ItemHomeCategoryBinding,
    private val context: Context,
    private val homeFragment: HomeFragment,
    private val holder: HomeScreenAdapter.CategoryViewHolder,
    private val homeScreenData: HomeScreenData
): ProvideCategory {

    private val categoryData = homeScreenData.movieItems

    override fun setUpWatchList() {
        setUpWatchList("Continue Watching", homeScreenData.watchList)
        Log.d("watchlistStatus", "watchlistcalled")
    }

    override fun setUpTrendingMovies() {
        setUpCategoryRV(
            "Trending Movies",
            categoryData.getMovies(0, 23)
        )
    }

    override fun setUpPopularShows() {
        mBinding.apply {
            setUpCategoryRV(
                "Popular Shows",
                categoryData.getMovies(24, 47)
            )
        }
    }

    override fun setUpLatestMovies() {
        mBinding.apply {
            setUpCategoryRV(
                "Latest Movies",
                categoryData.getMovies(48, 71)
            )
        }
    }

    override fun setUpNewTVShows() {
        mBinding.apply {
            setUpCategoryRV(
                "New TV Shows",
                categoryData.getMovies(72, 95)
            )
        }
    }

    override fun setUpComingSoon() {
        mBinding.apply {
            setUpCategoryRV(
                "Coming Soon",
                categoryData.getMovies(96, 120)
            )
        }
    }

    override fun onClick(pos: Int, data: MovieItem, cardView: CardView) {
        homeFragment.navigateToDetailFromCategory(cardView, holder, data)
    }

    override fun onClickWatchList(pos: Int, data: Movie, cardView: CardView) {
        homeFragment.navigateToDetailFromWatchList(cardView, holder, data)
    }

    private fun setUpCategoryRV(categoryTitle: String, data: List<MovieItem>){
        mBinding.apply {
            val adapter = MovieAdapter(context)
            tvCategoryTitle.text = categoryTitle
            rvCategoryItems.adapter = adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.isNestedScrollingEnabled = false;

            adapter.differ.submitList(data)
            adapter.setOnItemClickListener{pos, item, cardView ->
                onClick(pos, item, cardView)
            }
        }
    }

    private fun setUpWatchList(categoryTitle: String, data: List<Movie>){
        mBinding.apply {
            val adapter = WatchListAdapter(context)
            tvCategoryTitle.text = categoryTitle
            rvCategoryItems.adapter = adapter
            rvCategoryItems.layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvCategoryItems.setHasFixedSize(true);
            rvCategoryItems.isNestedScrollingEnabled = false;

            adapter.differ.submitList(data)
            adapter.setOnItemClickListener{pos, item, cardView ->
                onClickWatchList(pos, item, cardView)
            }
        }
    }

}

fun List<MovieItem>.getMovies(from: Int, to: Int): List<MovieItem>{
    val movieItems: ArrayList<MovieItem> = ArrayList()
    this.forEachIndexed{index, item ->
        if(index in from..to){
            movieItems.add(item)
        }
    }

    return movieItems
}