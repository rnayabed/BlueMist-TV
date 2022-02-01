package com.anatame.pickaflix.utils.parser

import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.SearchMovieItem
import com.anatame.pickaflix.utils.retrofit.VidData

interface ParserProvider {
    // HomeScreen Methods
    suspend fun getMovieList(page: Int = 0): ArrayList<MovieItem>
    suspend fun getHeroSectionItems() : ArrayList<HeroItem>

    // DetailScreen Methods
    suspend fun getMovieDetails(movieSrc: String): MovieDetails

    // get servers for movie type
    suspend fun getMovieServers(movieID: String): ArrayList<ServerItem>

    // get seasons for a show
    suspend fun getSeasons(showDataID: String): ArrayList<SeasonItem>

    // get episodes for a show
    suspend fun getEpisodes(seasonID: String): ArrayList<EpisodeItem>

    // get servers fow a show episode
    suspend fun getServers(episodeID: String): ArrayList<ServerItem>

    // get video source from servers
    suspend fun getVidSource(serverDataID: String): VidData

    // get searched item
    suspend fun getSearchItem(searchTerm: String): ArrayList<SearchMovieItem>


}