package com.anatame.pickaflix.ui.detail.handler

import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.common.utils.HeadlessWebViewHelper
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.DetailViewModel
import com.anatame.pickaflix.ui.detail.adapter.EpisodeRVAdapter
import com.anatame.pickaflix.ui.detail.models.EpisodeItem
import com.anatame.pickaflix.ui.detail.models.SeasonItem
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.utils.Resource
import okhttp3.internal.notifyAll

class DetailDataHandler(
    val context: Context,
    val activity: MainActivity,
    val binding: FragmentDetailBinding,
    val detailViewModel: DetailViewModel
) {

    private lateinit var streamUrl: String
    private lateinit var detailHandlerListener: DetailHandlerListener


    fun handleVidEmbedLinkLoaded(response: Resource<String>){
        response.data?.let { it ->
            getStreamUrl(it)
            binding.progressBar.visibility = View.GONE
            binding.loadingIcon.show()
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
            val epAdapter = EpisodeRVAdapter(it)
            binding.rvEpisodes.apply {
                visibility = View.VISIBLE
                adapter = epAdapter
                layoutManager = LinearLayoutManager(context)
            }

            epAdapter.setOnItemClickListener { position, epsID ->
                detailViewModel.getSelectedEpisodeVid(epsID)
                binding.progressBar.visibility = View.VISIBLE
                binding.loadingIcon.hide()
            }
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