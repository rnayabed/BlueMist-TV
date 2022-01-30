package com.anatame.pickaflix.model.scrollstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class HomeScrollStates (
    val heroScrollState : MutableLiveData<Int>,

    val trendingScrollState : MutableLiveData<Int>,
    val popularScrollState : MutableLiveData<Int>,
    val latestMoviesScrollState : MutableLiveData<Int>,
    val newTvShowsScrollState : MutableLiveData<Int>,
    val comingSoonTvScrollState : MutableLiveData<Int>,
)