package com.anatame.pickaflix.ui.detail.Composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails

@Composable
fun DetailScreen(
    episodeStateList: MutableState<List<EpisodeItem>?>,
    selectedItem: MutableState<Int>,
    movieDetails: MutableState<MovieDetails?>,
    onEpisodeClick: (id: Int, epsDataID: String) -> Unit
){
    LazyColumn(modifier = Modifier
        .fillMaxWidth()
        .fillMaxSize()) {
            episodeStateList.value?.let{
            itemsIndexed(it) { index, item ->
                if (item.title.isNotEmpty()) {
                    EpisodeView(
                        selectedItem,
                        index,
                        item.title,
                        item.episodeDataID,
                        onEpisodeClick
                    )
                }
            }
        }
    }
}