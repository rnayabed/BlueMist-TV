package com.anatame.pickaflix.model

import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem

data class HomeScreenData(
    val sliderItems: List<HeroItem>,
    val movieItems: List<MovieItem>
)
