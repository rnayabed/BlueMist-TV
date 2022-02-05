package com.anatame.pickaflix.ui.detail.handler.uihandlers

import android.content.Context
import android.view.View
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.Composables.DetailScreen
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails

class DetailUiHandlerImpl(
    fBinding: FragmentDetailBinding,
    val mContext: Context,
) : AbstractDetailUiHandler(fBinding, mContext) {

    private lateinit var episodeStateList : MutableState<List<EpisodeItem>?>
    private lateinit var movieDetails: MutableState<MovieDetails?>
    private lateinit var selectedItem: MutableState<Int>

    init {
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                movieDetails = remember { mutableStateOf(null) }
                episodeStateList = remember { mutableStateOf(null) }
                val selectedItem = remember { mutableStateOf(0) }

                DetailScreen(
                    episodeStateList = episodeStateList,
                    selectedItem = selectedItem,
                    movieDetails = movieDetails,
                    onEpisodeClick = { id, epsDataID, ->
                        selectedItem.value = id
                        getSelectedEpisode(epsDataID)
                    }
                )
            }
        }
    }

    override fun handleMovieDetailsLoaded(movieDetails: MovieDetails) {
        binding.tvMovieTitle.text = movieDetails.movieTitle
    }

    override fun handleEpisodeLoaded(episodes: List<EpisodeItem>){
        binding.progressBar.visibility = View.INVISIBLE
        binding.loadingIcon.show()
        episodeStateList.value = episodes
    }

}