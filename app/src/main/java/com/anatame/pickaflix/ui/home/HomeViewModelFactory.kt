package com.anatame.pickaflix.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anatame.pickaflix.utils.data.db.MovieDao

class HomeViewModelFactory(
    val db: MovieDao
) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(db) as T
    }
}