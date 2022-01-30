package com.anatame.pickaflix.ui.detail.models


data class MovieDetails(
    val movieUrl: String,
    val movieTrailerUrl: String,
    val movieDataID: String,
    val movieTitle : String,
    val movieQuality: String,
    val movieRating: String,
    val movieLength : String,
    val movieDescription : String,
    val movieBackgroundCoverUrl : String,
    var country: String,
    var genre: String,
    var released: String,
    var production: String,
    var casts: String,
)

