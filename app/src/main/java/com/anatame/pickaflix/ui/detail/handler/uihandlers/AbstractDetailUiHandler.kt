package com.anatame.pickaflix.ui.detail.handler.uihandlers

import android.R
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.handler.dataHandlers.AbstractDetailHandler
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.utils.Resource

abstract class AbstractDetailUiHandler(
    val binding: FragmentDetailBinding,
    val context: Context
) {

    private lateinit var detailUiHandlerListener: DetailUiHandlerListener

    abstract fun handleMovieDetailsLoaded(movieDetails: MovieDetails)
    abstract fun handleEpisodeLoaded(episodes: List<EpisodeItem>)

    open fun handleSeasonsLoaded(seasons: List<SeasonItem>){
        val spinner = binding.seasonSpinner
        val seasons = seasons

        Log.d("SeasonsData", seasons.toString())

        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(context,  R.layout.simple_spinner_dropdown_item,  seasons.map {
            it.seasonName
        })
        spinner.adapter = dataAdapter

        spinner.setOnItemSelectedListener(object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                callSeasonItemSelectedListener(seasons[position].seasonDataID)
                binding.progressBar.visibility = View.VISIBLE
                binding.loadingIcon.hide()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

    }

    protected open fun getSelectedEpisode(epsDataID: String){
        callEpisodeItemSelectedListener(epsDataID)
        binding.progressBar.visibility = View.VISIBLE
        binding.loadingIcon.hide()
    }

    open fun getVideoEmbedLinkLoaded(embedUrl: String){
        binding.progressBar.visibility = View.GONE
        binding.loadingIcon.show()
        callVideoEmbedLoadedListener(embedUrl)
    }

    private fun callEpisodeItemSelectedListener(episodeDataID: String){
        if(this::detailUiHandlerListener.isInitialized){
            detailUiHandlerListener.episodeItemSelected(episodeDataID)
        }
    }

    protected fun callSeasonItemSelectedListener(seasonDataID: String){
        if(this::detailUiHandlerListener.isInitialized){
            detailUiHandlerListener.seasonItemSelected(seasonDataID)
        }
    }

    protected fun callVideoEmbedLoadedListener(url: String){
        if(this::detailUiHandlerListener.isInitialized){
            detailUiHandlerListener.videoEmbedLinkLoaded(url)
        }
    }

    fun addListener(mDetailUiHandlerListener: DetailUiHandlerListener){
        detailUiHandlerListener = mDetailUiHandlerListener
    }

}