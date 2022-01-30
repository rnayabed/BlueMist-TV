package com.anatame.pickaflix.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.parser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(
    val movieDao: MovieDao
) : ViewModel() {

    val homeScreenData : MutableLiveData<Resource<HomeScreenData>> = MutableLiveData()
    private lateinit var sliderData: List<HeroItem>
    private lateinit var movieData: List<MovieItem>
    private lateinit var watchList: List<Movie>

    private var isNotFirstInit: Boolean = false

    init {
        getHomeScreenData()
    }

    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            homeScreenData.postValue(Resource.Loading())
            try {
                sliderData = Parser.getHeroSectionItems()
                movieData = Parser.getMovieList()
                watchList =  movieDao.getAll()

                homeScreenData.postValue(Resource.Success(HomeScreenData(
                    sliderData,
                    watchList,
                    movieData
                )))

            } catch (e: Exception){
                homeScreenData.postValue(Resource.Error("Something went wrong!"))
                e.printStackTrace()
            }
        }
    }

    fun updateHomeScreenData() {
        if (isNotFirstInit) {
            viewModelScope.launch(Dispatchers.IO) {

                watchList = movieDao.getAll()
                Log.d("homeViewModel", "fuck")
                homeScreenData.postValue(
                    Resource.Success(
                        HomeScreenData(
                            sliderData,
                            watchList,
                            movieData
                        )
                    )
                )
            }
        }

    }

    fun setFirstInit(){
        isNotFirstInit = true
    }

}