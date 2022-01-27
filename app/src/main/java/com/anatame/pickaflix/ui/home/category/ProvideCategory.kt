package com.anatame.pickaflix.ui.home.category

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.anatame.pickaflix.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.databinding.ItemHomeCategoryBinding

interface ProvideCategory {
    fun setUpTrendingMovies()
    fun setUpPopularShows()
    fun setUpLatestMovies()
    fun setUpNewTVShows()
    fun setUpComingSoon()
    fun onClick(pos: Int, data: MovieItem, cardView: CardView)
}