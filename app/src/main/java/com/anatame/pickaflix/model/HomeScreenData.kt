package com.anatame.pickaflix.model

import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem

data class HomeScreenData(
    val sliderItems: List<HeroItem>,
    val watchList: List<Movie>,
    val movieItems: List<MovieItem>
)
