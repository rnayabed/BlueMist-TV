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
import com.anatame.pickaflix.ui.detail.models.ServerItem
import com.anatame.pickaflix.utils.Resource

class DetailDataHandler(
    val context: Context,
    val activity: MainActivity,
    val binding: FragmentDetailBinding,
    val detailViewModel: DetailViewModel
) {

    private lateinit var streamUrl: String
    private lateinit var detailHandlerListener: DetailHandlerListener

    fun handleVidEmbedLinkLoaded(response: Resource<String>){
        val webPlayer = activity.getWebPlayer()
        response.data?.let { it ->
            getStreamUrl(webPlayer, it)
            binding.progressBar.visibility = View.GONE
            binding.loadingIcon.show()
        }
    }
    
    fun handleServersLoaded(response: Resource<List<ServerItem>>){
        val spinner = binding.serverSpinner
        val servers:List<ServerItem> = response.data!!

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
               detailHandlerListener.onServerItemSelected(p2, servers[p2])
                detailViewModel.getSelectedServerVid(servers[p2].serverDataId)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        })

        val dataAdapter: ArrayAdapter<String> = ArrayAdapter(context,  android.R.layout.simple_spinner_dropdown_item,  servers.map { serverItem ->
                serverItem.serverName
            })
        spinner.adapter = dataAdapter
    }

    fun handleEpisodeLoaded(response: Resource<List<EpisodeItem>>){
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

    private fun getStreamUrl(webPlayer: WebView, url: String) {
        val headlessWebViewHelper = HeadlessWebViewHelper(webPlayer, url)
        headlessWebViewHelper.setOnStreamUrlLoadedListener{
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