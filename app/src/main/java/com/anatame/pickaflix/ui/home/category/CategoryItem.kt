package com.anatame.pickaflix.ui.home.category

import android.content.Context
import android.util.Log
import androidx.cardview.widget.CardView
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.model.scrollstate.HomeScrollStates
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.childAdapters.MovieAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.childAdapters.WatchListAdapter
import com.anatame.pickaflix.utils.data.db.entities.Movie

class CategoryItem(
    private val mBinding: ItemHomeCategoryBinding,
    private val context: Context,
    private val homeFragment: HomeFragment,
    private val holder: HomeScreenAdapter.CategoryViewHolder,
    private val homeScreenData: HomeScreenData,
    private val hScrollStates: HomeScrollStates,
    private val lifecycleOwner: LifecycleOwner
): ProvideCategory {
    override fun setUpWatchList() {
        TODO("Not yet implemented")
    }

    override fun setUpTrendingMovies() {
        TODO("Not yet implemented")
    }

    override fun setUpPopularShows() {
        TODO("Not yet implemented")
    }

    override fun setUpLatestMovies() {
        TODO("Not yet implemented")
    }

    override fun setUpNewTVShows() {
        TODO("Not yet implemented")
    }

    override fun setUpComingSoon() {
        TODO("Not yet implemented")
    }

    override fun onClick(pos: Int, data: MovieItem, cardView: CardView) {
        TODO("Not yet implemented")
    }

    override fun onClickWatchList(pos: Int, data: Movie, cardView: CardView) {
        TODO("Not yet implemented")
    }

    override fun onLongClickWatchList(pos: Int, data: Movie, cardView: CardView) {
        TODO("Not yet implemented")
    }

//    private val categoryData = homeScreenData.movieItems
//
//    override fun setUpWatchList() {
//        setUpWatchList("Continue Watching", homeScreenData.watchList)
//        Log.d("watchlistStatus", "watchlistcalled")
//    }
//
//    override fun setUpTrendingMovies() {
//        setUpCategoryRV(
//            "Trending Movies",
//            categoryData.getMovies(0, 23),
//            1,
//            hScrollStates.trendingScrollState
//        )
//    }
//
//    override fun setUpPopularShows() {
//        mBinding.apply {
//            setUpCategoryRV(
//                "Popular Shows",
//                categoryData.getMovies(24, 47),
//                2,
//                hScrollStates.popularScrollState
//            )
//        }
//    }
//
//    override fun setUpLatestMovies() {
//        mBinding.apply {
//            setUpCategoryRV(
//                "Latest Movies",
//                categoryData.getMovies(48, 71),
//                3,
//                hScrollStates.latestMoviesScrollState
//            )
//        }
//    }
//
//    override fun setUpNewTVShows() {
//        mBinding.apply {
//            setUpCategoryRV(
//                "New TV Shows",
//                categoryData.getMovies(72, 95),
//                4,
//                hScrollStates.newTvShowsScrollState
//            )
//        }
//    }
//
//    override fun setUpComingSoon() {
//        mBinding.apply {
//            setUpCategoryRV(
//                "Coming Soon",
//                categoryData.getMovies(96, 120),
//                5,
//                hScrollStates.comingSoonTvScrollState
//            )
//        }
//    }
//
//    override fun onClick(pos: Int, data: MovieItem, cardView: CardView) {
//        homeFragment.navigateToDetailFromCategory(cardView, holder, data)
//    }
//
//    override fun onClickWatchList(pos: Int, data: Movie, cardView: CardView) {
//        homeFragment.navigateToDetailFromWatchList(cardView, holder, data)
//    }
//
//    override fun onLongClickWatchList(pos: Int, data: Movie, cardView: CardView){
//        homeFragment.handleWatchListLongClick(cardView, holder, data)
//    }
//
//    private fun setUpCategoryRV(
//        categoryTitle: String,
//        data: List<MovieItem>,
//        homePosition: Int,
//        scrollState: MutableLiveData<Int>
//    ){
//        mBinding.apply {
//            val adapter = MovieAdapter(context, homePosition)
//            tvCategoryTitle.text = categoryTitle
//            rvCategoryItems.adapter = adapter
//            rvCategoryItems.layoutManager = LinearLayoutManager(
//                context,
//                LinearLayoutManager.HORIZONTAL,
//                false
//            )
//            rvCategoryItems.setHasFixedSize(true);
//            rvCategoryItems.isNestedScrollingEnabled = false;
//
//            scrollState.observe(lifecycleOwner, Observer{
//                (rvCategoryItems.layoutManager as LinearLayoutManager)
//                    .scrollToPositionWithOffset(it, 200)
//            })
//
//            adapter.differ.submitList(data)
//            adapter.setOnItemClickListener{pos, item, cardView ->
//                onClick(pos, item, cardView)
//                scrollState.postValue(pos)
//            }
//        }
//    }
//
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

            adapter.setOnItemLongClickListener{pos, item, cardView ->
                onLongClickWatchList(pos, item, cardView)
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