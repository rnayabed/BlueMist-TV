package com.anatame.pickaflix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.parser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {


    private val _trendingMovies = MutableLiveData<Resource<List<MovieItem>>>()
    val trendingMovies: LiveData<Resource<List<MovieItem>>> = _trendingMovies


    init {
        getHomeScreenData()
    }

    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            _trendingMovies.postValue(Resource.Loading())
            try {
                val response = Parser.getMovieList()
                _trendingMovies.postValue(Resource.Success(response))
                Logger.Log(response)
            } catch (e: Exception){
                _trendingMovies.postValue(Resource.Error("Something went wrong!"))
                e.printStackTrace()
            }
        }
    }
}