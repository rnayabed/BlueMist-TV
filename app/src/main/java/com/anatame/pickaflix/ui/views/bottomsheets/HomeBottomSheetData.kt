package com.anatame.pickaflix.ui.views.bottomsheets

import com.anatame.pickaflix.ui.home.HomeFragment
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.data.db.entities.Movie
import java.io.Serializable

data class HomeBottomSheetData(
    val homeFragment: HomeFragment,
    val movie: Movie
) : Serializable