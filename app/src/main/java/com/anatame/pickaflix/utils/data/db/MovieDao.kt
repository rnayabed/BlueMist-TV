package com.anatame.pickaflix.utils.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anatame.pickaflix.utils.data.db.entities.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>
}