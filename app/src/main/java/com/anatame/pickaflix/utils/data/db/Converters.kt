package com.anatame.pickaflix.utils.data.db

import androidx.room.TypeConverter
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromSliderList(value: List<HeroItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<HeroItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSliderList(value: String): List<HeroItem> {
        val gson = Gson()
        val type = object : TypeToken<List<HeroItem>>() {}.type
        return gson.fromJson(value, type)
    }


    @TypeConverter
    fun fromMovieList(value: List<MovieItem>): String {
        val gson = Gson()
        val type = object : TypeToken<List<MovieItem>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMovieList(value: String): List<MovieItem> {
        val gson = Gson()
        val type = object : TypeToken<List<MovieItem>>() {}.type
        return gson.fromJson(value, type)
    }

}