package com.anatame.pickaflix.utils.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "movies"
)
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val source: String,
    val title: String,
    val thumbnailUrl: String,
    val movieType: String
) : Serializable