package com.anatame.pickaflix.ui.home.category

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding
import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.home.adapter.MovieAdapter

class CategoryItem(
    private val mBinding: ItemHomeCategoryBinding,
    private val context: Context,
    private val homeFragment: HomeFragment,
    private val holder: HomeScreenAdapter.CategoryViewHolder
): ProvideCategory {
    override fun setUpTrendingMovies() {
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
            adapter.setOnItemClickListener{pos, item, cardView ->
                onClick(pos, item, cardView)
            }
        }
    }

    override fun setUpPopularShows() {
        mBinding.apply {
            tvCategoryTitle.text = "Popular Shows"
        }
    }

    override fun setUpLatestMovies() {
        mBinding.apply {
            tvCategoryTitle.text = "Latest Movies"
        }
    }

    override fun setUpNewTVShows() {
        mBinding.apply {
            tvCategoryTitle.text = "New TV Shows"
        }
    }

    override fun setUpComingSoon() {
        mBinding.apply {
            tvCategoryTitle.text = "Coming Soon!"
        }
    }


    override fun onClick(pos: Int, data: MovieItem, cardView: CardView) {
        homeFragment.navigateToDetailFromCategory(cardView, holder)
    }

}