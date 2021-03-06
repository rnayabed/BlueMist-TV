package com.anatame.pickaflix.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.parser.ParserProvider

class HomeViewModelFactory(
    val db: MovieDao,
    val parser: ParserProvider
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(db, parser) as T
    }
}