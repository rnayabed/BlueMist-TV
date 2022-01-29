package com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO

import java.io.Serializable

data class HeroItem(
    val backgroundImageUrl: String,
    val title: String,
    val source: String,
    val duration: String,
    val rating: String
): Serializable
