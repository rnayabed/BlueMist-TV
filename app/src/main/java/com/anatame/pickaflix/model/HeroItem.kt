package com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO

import java.io.Serializable

data class HeroItem(
    val thumbnailUrl: String,
    val title: String,
    val Url: String,
    val length: String,
    val rating: String,
    val movieType: String
): Serializable
