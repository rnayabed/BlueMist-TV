package com.anatame.pickaflix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.Logger
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.parser.Parser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel : ViewModel() {


    val homeScreenData : MutableLiveData<Resource<HomeScreenData>> = MutableLiveData()

    init {
        getHomeScreenData()
    }


    fun getHomeScreenData(){
        viewModelScope.launch(Dispatchers.IO) {
            homeScreenData.postValue(Resource.Loading())
            try {
                val sliderData = Parser.getHeroSectionItems()
                val movieData = Parser.getMovieList()
                homeScreenData.postValue(Resource.Success(HomeScreenData(
                    sliderData,
                    movieData
                )))

            } catch (e: Exception){
                homeScreenData.postValue(Resource.Error("Something went wrong!"))
                e.printStackTrace()
            }
        }
    }

}