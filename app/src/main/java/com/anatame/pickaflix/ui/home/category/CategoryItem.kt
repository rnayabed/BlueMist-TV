package com.anatame.pickaflix.ui.home.category

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.home.adapter.MovieAdapter
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieDetails

class CategoryItem(
    private val mBinding: ItemHomeCategoryBinding,
    private val context: Context,
    private val homeFragment: HomeFragment,
    private val holder: HomeScreenAdapter.CategoryViewHolder,
    private val categoryData: List<MovieItem>
): ProvideCategory {
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
        homeFragment.navigateToDetailFromCategory(cardView, holder)
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