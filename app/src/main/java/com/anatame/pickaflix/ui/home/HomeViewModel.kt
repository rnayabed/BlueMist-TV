package com.anatame.pickaflix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.parser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {


    val sliderItems: MutableLiveData<Resource<List<HeroItem>>> = MutableLiveData()

    val movieItems: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()

    val trendingMovies = MutableLiveData<Resource<List<MovieItem>>>()
    val trendingShows: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val latestMovies: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val latestShows: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()
    val comingSoon: MutableLiveData<Resource<List<MovieItem>>> = MutableLiveData()


    init {
        getHomeScreenData()
    }

    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            trendingMovies.postValue(Resource.Loading())
            try {
                val response = Parser.getMovieList()
                Logger.Log(response)
                movieItems.postValue(Resource.Success(response))

                val trendingMovieItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 0..23){
                        trendingMovieItems.add(item)
                    }
                }

                val trendingShowItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 24..47){
                        trendingShowItems.add(item)
                    }
                }

                val latestMovieItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 48..71){
                        latestMovieItems.add(item)
                    }
                }

                val latestShowItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 72..95){
                        latestShowItems.add(item)
                    }
                }

                val comingSoonItems: ArrayList<MovieItem> = ArrayList()
                response.forEachIndexed{index, item ->
                    if(index in 96..120){
                        comingSoonItems.add(item)
                    }
                }

                trendingMovies.postValue(Resource.Success(trendingMovieItems))
                trendingShows.postValue(Resource.Success(trendingShowItems))
                latestMovies.postValue(Resource.Success(latestMovieItems))
                latestShows.postValue(Resource.Success(latestShowItems))
                comingSoon.postValue(Resource.Success(comingSoonItems))

            } catch (e: Exception){
                trendingMovies.postValue(Resource.Error("Something went wrong!"))
                e.printStackTrace()
            }
        }
    }
}