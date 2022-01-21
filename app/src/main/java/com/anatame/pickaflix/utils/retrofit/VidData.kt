package com.anatame.pickaflix.utils.retrofit

data class VidData(
    val link: String,
    val sources: List<Any>,
    val title: String,
    val tracks: List<Any>,
    val type: String
)