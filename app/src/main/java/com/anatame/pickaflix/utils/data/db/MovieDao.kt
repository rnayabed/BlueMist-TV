package com.anatame.pickaflix.utils.data.db

import androidx.room.*
import com.anatame.pickaflix.utils.data.db.entities.HomeScreenCache
import com.anatame.pickaflix.utils.data.db.entities.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertHomeScreenData(homeScreenData: HomeScreenCache)

    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM homeScreenCache")
    fun getHomeScreenData(): HomeScreenCache

    @Delete
    fun deleteMovieFromWatchList(movie: Movie)

}