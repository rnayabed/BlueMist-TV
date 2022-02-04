package com.anatame.pickaflix.utils.parser

import android.util.Log
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.utils.constants.Constants
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.SearchMovieItem
import com.anatame.pickaflix.utils.retrofit.VidData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Parser2 : ParserProvider {
    val homePageUrl = "https://fmovies.to/home"

    private fun getDoc(url: String): Document {
        return Jsoup.connect(homePageUrl)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36.")
            .maxBodySize(0)
            .timeout(1000*5)
            .get()
    }

    override suspend fun getMovieList(page: Int): ArrayList<MovieItem> {
        var movieItemListData = ArrayList<MovieItem>()
        val doc = getDoc(homePageUrl)

        val movieItemList =  doc.getElementsByClass("item")
        movieItemList.forEach { element ->
            if( element.attr("data-src").isEmpty() || element.attr("data-src") == null){
                val movieItem = element.allElements[0]
                // Log.d("parser2", movieItem.toString())



                val image = movieItem.select("img")
                val imgSrcLowRes = image.attr("src")
                val imageSrc = imgSrcLowRes.substring(0..imgSrcLowRes.indexOf(".jpg")+3)

                val anchor = movieItem.select("a")
                val movieHref = anchor.attr("href")
                val movieTitle = anchor.attr("title")

                val meta = movieItem.getElementsByClass("meta")
                var movieReleaseDate = ""
                var movieLength = ""
                var movieType = meta[0].getElementsByClass("type").text()


                movieReleaseDate = meta.textNodes()[0].toString().trim()
                movieLength = meta.textNodes()[1].toString().trim()


                val movieItemData = MovieItem(
                    title = movieTitle,
                    thumbnailUrl = imageSrc,
                    Url = movieHref,
                    releaseDate = movieReleaseDate!!,
                    length = movieLength,
                    movieType = movieType,
                    quality = ""
                )

                movieItemListData.add(movieItemData)

                Log.d("parser2", """
                    $imageSrc
                    $movieHref
                    $movieTitle
                    $movieLength
                    $movieReleaseDate
                    $movieType
                """.trimIndent())

            }
        }

//        movieItemList.forEach { element ->
//            val movieItem = element.allElements[0]
//
//            val image = movieItem.select("img")
//            val imageSrc = image.attr("data-src")
//
//            val anchor = movieItem.select("a")
//            val movieHref = anchor.attr("href")
//            val movieTitle = anchor.attr("title")
//
//            val movieFdiItem = movieItem.getElementsByClass("fdi-item")
//            val movieReleaseDate = movieFdiItem[0]?.text()
//            var movieLength = movieItem.getElementsByClass("fdi-duration").text()
//            val movieQuality =  movieItem.getElementsByClass("pick film-poster-quality").text()
//            val movieType = movieItem.getElementsByClass("fdi-type").text()
//
//            if(movieType == "TV"){
//                for(it in movieItem
//                    .getElementsByClass("film-detail")
//                    .select("span")){
//
//                    if(it.text().contains("EPS")) {
//                        movieLength = it.text()
//                        break
//                    }
//                }
//            }
//
//            if(movieLength.isEmpty()) movieLength = movieType
//
//            Log.d("movieEps", """
//                   $movieType
//                   $movieTitle
//                   ${movieLength}
//                   ${movieLength.length}
//                   """.trimIndent())
////            LogMovieItems(
////                movieTitle,
////                movieHref,
////                imageSrc,
////                movieReleaseDate,
////                movieLength,
////                movieQuality,
////                movieType
////            )
//
//            val movieItemData = MovieItem(
//                title = movieTitle,
//                thumbnailUrl = imageSrc,
//                Url = movieHref,
//                releaseDate = movieReleaseDate!!,
//                length = movieLength,
//                quality = movieQuality,
//                movieType = movieType
//            )
//
//            movieItemListData.add(movieItemData)
//        }

        return movieItemListData

//            element.allElements.forEachIndexed { index, mElement ->
//               val item = mElement.getElementsByClass("film-poster")[index]
//                Log.d(MOVIE_TAG1, item.select("img")[0].absUrl("src"))
//            }
    }

    override suspend fun getHeroSectionItems(): ArrayList<HeroItem> {
        val heroItem = ArrayList<HeroItem>()

        heroItem.add(HeroItem(
            "",
            "",
            "",
            "",
            "",
            "",
        ))
        return heroItem
    }

    override suspend fun getMovieDetails(movieSrc: String): MovieDetails {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieServers(movieID: String): ArrayList<ServerItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getSeasons(showDataID: String): ArrayList<SeasonItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getEpisodes(seasonID: String): ArrayList<EpisodeItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getServers(episodeID: String): ArrayList<ServerItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getVidSource(serverDataID: String): VidData {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchItem(searchTerm: String): ArrayList<SearchMovieItem> {
        TODO("Not yet implemented")
    }

}

