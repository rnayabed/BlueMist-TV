package com.anatame.pickaflix.utils.data.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem

@Entity( tableName = "homeScreenCache")
data class HomeScreenCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val sliderItems: List<HeroItem>,
    val movieItems: List<MovieItem>
)
