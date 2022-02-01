package com.anatame.pickaflix.ui.detail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.utils.parser.Parser
import com.anatame.pickaflix.utils.parser.ParserProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    parser: ParserProvider
): ViewModel() {

    val movieDetails: MutableLiveData<Resource<MovieDetails>> = MutableLiveData()
    val vidEmbedLink: MutableLiveData<Resource<String>> = MutableLiveData()
    val serverList: MutableLiveData<Resource<List<ServerItem>>> = MutableLiveData()
    val seasonList: MutableLiveData<Resource<List<SeasonItem>>> = MutableLiveData()
    val episodeList: MutableLiveData<Resource<List<EpisodeItem>>> = MutableLiveData()


    fun getMovieData(url: String){
        val movieDataID: String = url.substring(url.lastIndexOf("-") + 1, (url.length))
        Log.d("movieSeasons","$url $movieDataID")
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val servers = Parser.getMovieServers(movieDataID)
                Log.d("movieSeasons", servers.toString())

                serverList.postValue(Resource.Success(servers))

                val vidSrc = Parser.getVidSource(servers.getVidCloud().serverDataId)
                Log.d("movieSeasons", vidSrc.toString())
                Log.d("vidPlayerTest", "gotVideoSrc")
                vidEmbedLink.postValue(Resource.Success(vidSrc.link))

            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun getSeasons(url: String){
        val showDataID: String = url.substring(url.lastIndexOf("-") + 1, (url.length))
        Log.d("movieSeasons","$url $showDataID")
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val begin = System.currentTimeMillis()
//                movieDetails.postValue(Resource.Loading())
                val response = Parser.getSeasons(showDataID)
//                movieDetails.postValue(Resource.Success(response))
                Log.d("movieSeasons", response.toString())
                seasonList.postValue(Resource.Success(response))

                val eps = Parser.getEpisodes(response.first().seasonDataID)
                episodeList.postValue(Resource.Success(eps))
                Log.d("movieSeasons", eps.toString())

                val servers = Parser.getServers(eps.first().episodeDataID)
                serverList.postValue(Resource.Success(servers))
                Log.d("movieSeasons", servers.toString())

                val vidSrc = Parser.getVidSource(servers.getVidCloud().serverDataId)
                Log.d("movieSeasons", vidSrc.toString())
                Log.d("vidPlayerTest", "gotVideoSrc")
                vidEmbedLink.postValue(Resource.Success(vidSrc.link))

                Log.d("movieSeasons", (System.currentTimeMillis() - begin).toString())

            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

    fun getEpisodes(seasonDataID: String){
        viewModelScope.launch (Dispatchers.IO){
            val eps = Parser.getEpisodes(seasonDataID)
            episodeList.postValue(Resource.Success(eps))
        }
    }

    fun getSelectedEpisodeVid(episodeDataID: String){
        viewModelScope.launch (Dispatchers.IO) {
            val servers = Parser.getServers(episodeDataID)
            serverList.postValue(Resource.Success(servers))
            Log.d("movieSeasons", servers.toString())


            val vidSrc = Parser.getVidSource(servers.getVidCloud().serverDataId)
            Log.d("movieSeasons", vidSrc.toString())
            vidEmbedLink.postValue(Resource.Success(vidSrc.link))
        }
    }

//    fun getSelectedServerVid(serverDataId: String){
//        viewModelScope.launch (Dispatchers.IO) {
//            val vidSrc = Parser.getVidSource(serverDataId)
//            Log.d("movieSeasons", vidSrc.toString())
//            vidEmbedLink.postValue(Resource.Success(vidSrc.link))
//        }
//    }


    fun getMovieDetails(movieSrc: String){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                movieDetails.postValue(Resource.Loading())
                val response = Parser.getMovieDetails(movieSrc)
                Log.d("checkData", response.movieTitle)
                movieDetails.postValue(Resource.Success(response))
            } catch (e: Exception){
                e.printStackTrace()
            }

        }
    }

}

fun List<ServerItem>.getVidCloud(): ServerItem{
    lateinit var vidCloud: ServerItem
    this.forEach{
        if(it.serverName.contains("Vidcloud")){
            vidCloud = it
        }
    }

    return vidCloud
}