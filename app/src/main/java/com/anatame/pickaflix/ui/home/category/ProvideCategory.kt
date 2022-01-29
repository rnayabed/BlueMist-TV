package com.anatame.pickaflix.ui.home.category

import androidx.cardview.widget.CardView
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem

interface ProvideCategory {
    fun setUpTrendingMovies()
    fun setUpPopularShows()
    fun setUpLatestMovies()
    fun setUpNewTVShows()
    fun setUpComingSoon()
    fun onClick(pos: Int, data: MovieItem, cardView: CardView)
}