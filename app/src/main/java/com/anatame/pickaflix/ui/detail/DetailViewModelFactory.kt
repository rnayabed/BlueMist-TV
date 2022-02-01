package com.anatame.pickaflix.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.parser.ParserProvider

class DetailViewModelFactory(
    val parser: ParserProvider
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(parser) as T
    }
}