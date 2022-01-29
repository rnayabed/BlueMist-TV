package com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO

import java.io.Serializable

data class SearchMovieItem(
    val thumbnailSrc: String,
    val title: String,
    val src: String
) : Serializable