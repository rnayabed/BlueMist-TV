package com.anatame.pickaflix.utils.parser

import android.util.Log
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.*
import com.anatame.pickaflix.utils.data.remote.retrofit.RetrofitInstance
import com.anatame.pickaflix.utils.constants.Constants.MOVIE_LIST_SELECTOR
import com.anatame.pickaflix.utils.constants.Constants.MOVIE_URL
import com.anatame.pickaflix.utils.retrofit.VidData
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup


const val MOVIE_TAG1 = "movieItemList"

object Parser3 : ParserProvider {
    override suspend fun getVidSource(serverDataID: String): VidData {
        val vidData = RetrofitInstance.api.getVidData("https://hdtoday.tv/ajax/get_link/$serverDataID")
        return vidData.body()!!
    }

    override suspend fun getMovieServers(movieID: String): ArrayList<ServerItem> {
        val serverList = ArrayList<ServerItem>()

        val doc = Jsoup.connect("https://hdtoday.tv/ajax/movie/episodes/$movieID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        var serverDataID = ""
        var serverName = ""
        doc.body().select("a").forEach { item ->
            serverName = item.text()
            serverDataID = item.attr("data-linkid")

            serverList.add(
                ServerItem(
                    serverName,
                    serverDataID
                )
            )
        }

        Log.d("serverList", """
            $serverList
        """.trimIndent())

        return serverList
    }
    override suspend fun getServers(episodeID: String): ArrayList<ServerItem> {
        val serverList = ArrayList<ServerItem>()

        val doc = Jsoup.connect("https://hdtoday.tv/ajax/v2/episode/servers/$episodeID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        var serverDataID = ""
        var serverName = ""
        doc.body().select("a").forEach { item ->
            serverName = item.text()
            serverDataID = item.attr("data-id")

            serverList.add(
                ServerItem(
                    serverName,
                    serverDataID
                )
            )
        }

        Log.d("serverList", """
            $serverList
        """.trimIndent())

        return serverList
    }

    override suspend fun getEpisodes(seasonID: String): ArrayList<EpisodeItem>{
        val episodeList = ArrayList<EpisodeItem>()

        val doc = Jsoup.connect("https://hdtoday.tv/ajax/v2/season/episodes/$seasonID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        val episodeItems = doc.getElementsByClass("eps-item")
        episodeItems.forEach { item ->
            episodeList.add(
                EpisodeItem(item.attr("title"), item.attr("data-id"))
            )
        }
        getServers(episodeList[0].episodeDataID)
        return episodeList
    }

    override suspend fun getSeasons(showDataID: String): ArrayList<SeasonItem>{
        val seasonList = ArrayList<SeasonItem>()

        Log.d("movieSeasons","called from parser")
        val doc = Jsoup.connect("https://hdtoday.tv/ajax/v2/tv/seasons/$showDataID")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()

        val dropDownItem = doc.getElementsByClass("dropdown-item")
        dropDownItem.forEach { item ->
            seasonList.add(
                SeasonItem(item.text(), item.attr("data-id"))
            )
        }

        return seasonList
    }

//    fun getMovieDetailsFrom(movieSrc: String = "/tv/watch-the-flash-online-39535"){
//        val doc = Jsoup.connect("https://$HOST$movieSrc").get()
//        Log.d("detailFromPS", doc.toString())
//
//        val movieDetails = doc.getElementById("")
//    }

    override suspend fun getMovieDetails(movieSrc: String): MovieDetails {
        lateinit var movieDetail: MovieDetails

        val url = "https://hdtoday.tv${movieSrc}"
        val doc = Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000 * 5)
            .get()


        Log.d("getMovieDetails", "Called $doc")

        val statsContainer = doc.body().getElementsByClass("stats")


        var movieRating = doc.body().getElementsByClass("btn-imdb").text()
        val movieQuality = doc.body().getElementsByClass("btn-quality").text()
        val movieDataID = doc.body().getElementsByClass("detail_page-watch").attr("data-id")
        val movieTitle = doc.body().getElementsByClass("heading-name").text()
        val movieTrailer = doc.body().getElementById("iframe-trailer")
            ?.attr("data-src")
            .toString()
        var country = ""
        var genre = ""
        var released = ""
        var production = ""
        var casts = ""
        val movieDescription = ""
        val movieBackgroundCoverUrl =
            doc.body().getElementsByClass("cover_follow").attr("style").getBackgroundImageUrl()

        var movieLength = ""


        val elements = doc.body().getElementsByClass("row-line")
        elements.forEach { item ->
            if (item.select("span").text() == "Country:") country = item.select("a").text()
            if (item.select("span").text() == "Genre:") {
                item.select("a").forEach { g ->
                    if(genre.isNotEmpty()){
                        genre += ", ${g.text()}"
                    } else {
                        genre = g.text()
                    }
                }
            }
            if (item.select("span").text() == "Released:") released = item.text()
            if (item.select("span").text() == "Production:") {
                item.select("a").forEach { p ->
                    if(production.isNotEmpty()){
                        production += ", ${p.text()}"
                    } else {
                        production = p.text()
                    }

                }
            }

            if (item.select("span").text() == "Duration:") {
                movieLength = item.text()
            }
            if (item.select("span").text() == "Casts:") {
                item.select("a").forEach { c ->
                    if(casts.isNotEmpty()){
                        casts += ", ${c.text()}"
                    } else {
                        casts = c.text()
                    }
                }
            }
        }

        Log.d("movieTitleBruh", """
           $movieTrailer,
            $movieDataID,
            $movieTitle,
            $movieQuality,
            $movieRating,
            $movieLength,
            $movieDescription,
            $movieBackgroundCoverUrl,
            $country,
            $genre,
            $released,
            $production,
            $casts
            """.trimIndent())

        movieDetail = MovieDetails(
            "https://hdtoday.tv/watch-${movieSrc}",
            movieTrailer,
            movieDataID,
            movieTitle,
            movieQuality,
            movieRating,
            movieLength,
            movieDescription,
            movieBackgroundCoverUrl,
            country,
            genre,
            released,
            production,
            casts
        )

        return movieDetail
    }

    override suspend fun getSearchItem(searchTerm: String): ArrayList<SearchMovieItem> {
        val searchItemList = ArrayList<SearchMovieItem>()

        val client = OkHttpClient()
        val formBody= FormBody.Builder()
            .addEncoded("keyword", "$searchTerm")
            .build()

        val request = Request.Builder()
            .url("https://hdtoday.tv/ajax/search")
            .addHeader("authority", "hdtoday.tv")
            .addHeader("accept", "*/*")
            .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("origin", "https://hdtoday.tv/")
            .addHeader("referer", "https://hdtoday.tv/search/")
            .addHeader("sec-ch-ua-mobile", "?0")
            .addHeader("sec-ch-ua-platform", "Windows")
            .addHeader("sec-fetch-mode", "cors")
            .addHeader("sec-fetch-site", "same-origin")
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36")
            .addHeader("x-requested-with", "XMLHttpRequest")

            .post(formBody)
            .build()

        // Execute request
        val response = client.newCall(request).execute()
        val result = response.body?.string()
        Log.d("okResponse", result.toString())
        Log.d("okResponse", searchTerm)

        client.cache?.delete()

        val doc = Jsoup.parse(result!!)


        val movies = doc.select("a")
        movies.forEach { item ->
            val movieSrc = item.select("a").attr("href")
            val moviePoster = item.select("img").attr("src")
            val movieName = item.getElementsByClass("film-name").text()

            Log.d("searchReturn", """
               MovieSource = $movieSrc
               MoviePoster = $moviePoster
               MovieName = $movieName
            """.trimIndent())

            if(moviePoster.isNotEmpty()){
                searchItemList.add(SearchMovieItem(
                    thumbnailSrc = moviePoster,
                    title = movieName,
                    src = movieSrc
                ))
            }
        }

        return searchItemList

    }

    override suspend fun getHeroSectionItems() : ArrayList<HeroItem>{
        val heroItemList = ArrayList<HeroItem>()


        return heroItemList
    }

    override suspend fun getMovieList(page: Int): ArrayList<MovieItem> {
        var movieItemListData = ArrayList<MovieItem>()
        val doc = Jsoup.connect("https://hdtoday.tv/home")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
            .maxBodySize(0)
            .timeout(1000*5)
            .get()

        val movieItemList =  doc.getElementsByClass("flw-item")

        movieItemList.forEach { element ->
            val movieItem = element.allElements[0]

            Log.d("parser3", movieItem.toString())

            val image = movieItem.select("img")
            val imageSrc = image.attr("data-src")

            Log.d("parser3image", imageSrc.toString())

            val anchor = movieItem.select("a")
            val movieHref = anchor.attr("href")
            val movieTitle = anchor.attr("title")

            val movieFdiItem = movieItem.getElementsByClass("fdi-item")
            val movieReleaseDate = movieFdiItem[0]?.text()
            var movieLength = movieItem.getElementsByClass("fdi-duration").text()
            val movieQuality =  movieItem.getElementsByClass("pick film-poster-quality").text()
            val movieType = movieItem.getElementsByClass("fdi-type").text()

            if(movieType == "TV"){
                for(it in movieItem
                    .getElementsByClass("film-detail")
                    .select("span")){

                    if(it.text().contains("EPS")) {
                        movieLength = it.text()
                        break
                    }
                }
            }

            if(movieLength.isEmpty()) movieLength = movieType

            Log.d("movieEps", """
                   $movieType
                   $movieTitle
                   ${movieLength}
                   ${movieLength.length}
                   """.trimIndent())
            LogMovieItems(
                movieTitle,
                movieHref,
                imageSrc,
                movieReleaseDate,
                movieLength,
                movieQuality,
                movieType
            )

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
        }

        return movieItemListData

//            element.allElements.forEachIndexed { index, mElement ->
//               val item = mElement.getElementsByClass("film-poster")[index]
//                Log.d(MOVIE_TAG1, item.select("img")[0].absUrl("src"))
//            }
    }

    private fun LogMovieItems(
        movieTitle: String?,
        movieHref: String?,
        imageSrc: String?,
        movieReleaseDate: String?,
        movieLength: String?,
        movieQuality: String?,
        movieType: String?
    ) {
        Log.d(
            MOVIE_TAG1, """
                    Movie Title = $movieTitle
                    Movie Href = https://fmoviesto.cc$movieHref
                    Movie src = $imageSrc
                    Movie release date = $movieReleaseDate
                    Movie length = $movieLength
                    Movie quality = $movieQuality
                    Movie type = $movieType
                    
                    """.trimIndent()
        )
    }
    fun getHttpSearchItem() {
        var client = OkHttpClient()
        val formBody= FormBody.Builder()
            .add("keyword", "Venom")
            .build()

        var request = Request.Builder()
            .url("https://hdtoday.tv/ajax/search")
            .post(formBody)
            .build()

        // Execute request
        val response = client.newCall(request).execute()
        val result = response.body?.string()
        Log.d("okResponse", result.toString())


        val doc = Jsoup.parse(result!!)

        val movies = doc.select("a")
        movies.forEach { item ->
            val movieSrc = item.select("a").attr("href")
            val moviePoster = item.select("img").attr("src")
            val movieName = item.getElementsByClass("film-name").text()

            Log.d(
                "searchReturn", """
               MovieSource = $movieSrc
               MoviePoster = $moviePoster
               MovieName = $movieName
            """.trimIndent()
            )

        }
    }
}




