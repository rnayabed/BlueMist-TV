package com.anatame.pickaflix.ui.detail.handler

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.DetailViewModel
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.MovieDetails
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.compose.ui.Primary
import com.anatame.pickaflix.utils.compose.ui.PrimaryVariant

class DetailDataHandler(
    val lifecycleOwner: LifecycleOwner,
    val context: Context,
    val activity: MainActivity,
    val binding: FragmentDetailBinding,
    val detailViewModel: DetailViewModel
) {

    private lateinit var streamUrl: String
    private lateinit var detailHandlerListener: DetailHandlerListener
    private lateinit var episodeStateList : MutableState<List<EpisodeItem>>
    private lateinit var movieDetails: MutableState<MovieDetails?>


    fun initCompose(){
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {

                movieDetails = remember { mutableStateOf(null) }
                episodeStateList = remember { mutableStateOf(listOf(
                    EpisodeItem(
                        "",
                        ""
                    )
                ))}
                val selectedItem = remember { mutableStateOf(0) }

                detailViewModel.selectedEps.observe(lifecycleOwner, Observer {
                    selectedItem.value = it!!
                })

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize(),
                ) {
                    itemsIndexed(episodeStateList.value){index, item ->
                        if(item.title.isNotEmpty()){
                            EpisodeView(selectedItem, index, item.title, item.episodeDataID)
                        }
                    }

                    movieDetails.value?.let{
                        item {
                            Row() {
                                Text(text = "IMDB Ratings: ${it.movieRating}")
                            }
                        }

                        item {
                            Row() {
                                Text(text = "Genre ${it.genre}")
                            }
                        }

                        item {
                            Row() {
                                Text(text = "Casts ${it.casts}")
                            }
                        }

                        item {
                            Row() {
                                Text(text = "Released ${it.released}")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun EpisodeView(
        selectedIndex: MutableState<Int>,
        id: Int,
        episodeName: String,
        episodeDataID: String
    ) {

        val selected = remember { mutableStateOf(false) }

        Button(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (id == selectedIndex.value) Primary else Color.DarkGray),
            onClick = {
                detailViewModel.selectedEps.postValue(id)
                Log.d("fromComposeEps", episodeName)
                detailViewModel.getSelectedEpisodeVid(episodeDataID)
                binding.progressBar.visibility = View.VISIBLE
                binding.loadingIcon.hide()
            }
        ) {
            Text(
                text = "$episodeName",
                color = Color.White
            )
        }
    }

    fun handleMovieDetailsLoaded(response: Resource<MovieDetails>) {
        response.data?.let {
            binding.tvMovieTitle.text = it.movieTitle
            movieDetails.value = it
        }
    }


    fun handleSeasonsLoaded(response: Resource<List<SeasonItem>>){
        val spinner = binding.seasonSpinner
        val seasons = response.data!!

        Log.d("SeasonsData", seasons.toString())

        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(context,  android.R.layout.simple_spinner_dropdown_item,  seasons.map {
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
                detailViewModel.getEpisodes(seasons[position].seasonDataID)
                Log.d("easonsData", "bruh")
                binding.progressBar.visibility = View.VISIBLE
                binding.loadingIcon.hide()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

    }

    fun handleEpisodeLoaded(response: Resource<List<EpisodeItem>>){

        binding.progressBar.visibility = View.INVISIBLE

        response.data?.let {
            episodeStateList.value = it
        }
    }

    fun handleVidEmbedLinkLoaded(response: Resource<String>){
        response.data?.let { it ->
            getStreamUrl(it)
            binding.progressBar.visibility = View.GONE
            binding.loadingIcon.show()
        }
    }

    private fun getStreamUrl(url: String) {
        val webHelper = activity.getWebPlayer()
        Log.d("fromdataHandler", "Called $url")
        webHelper.loadUrl(url)
        webHelper.setOnStreamUrlLoadedListener{
            Log.d("streamUrl", it)
            binding.loadingIcon.hide()

            detailHandlerListener.onVideoUrlLoaded(it)

            binding.fullscreenBtn.visibility = View.VISIBLE
        }
    }

    fun addListener(mDetailHandlerListener: DetailHandlerListener){
        detailHandlerListener = mDetailHandlerListener
    }

}