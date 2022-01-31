package com.anatame.pickaflix.ui.detail

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.handler.DetailDataHandler
import com.anatame.pickaflix.ui.detail.handler.DetailHandlerListener
import com.anatame.pickaflix.utils.PlayerHelper
import com.anatame.pickaflix.utils.PlayerLoader
import com.anatame.pickaflix.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.transition.MaterialContainerTransform
import android.widget.ArrayAdapter
import com.anatame.pickaflix.ui.detail.models.ServerItem

import java.util.ArrayList


class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var vidHelper: PlayerHelper
    private lateinit var playerLoader: PlayerLoader
    private var streamUrl = ""
    private lateinit var dataHandler: DetailDataHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        args.cvTransitionId?.let {
            ViewCompat.setTransitionName(binding.cvDetailContainer, it)
        }

        Toast.makeText(context, "Hero Card ${ args.cvTransitionId } is equal to ${binding.cvDetailContainer.transitionName}", Toast.LENGTH_SHORT).show()

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_activity_main
            duration = 300
            scrimColor = Color.TRANSPARENT
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loadingIcon.hide()
        // Spinner element


        dataHandler = context?.let {
            DetailDataHandler(
                it,
                activity as MainActivity,
                binding,
                detailViewModel
            )
        }!!


        binding.fullscreenBtn.setOnClickListener {
            goFullScreen()
        }

        args.heroItem?.let {
            setUpScreen(it.thumbnailUrl, it.Url, it.movieType)
            Log.d("bruh", it.movieType)
        }

        args.watchList?.let{
            setUpScreen(it.thumbnailUrl, it.source, it.movieType)
        }

        args.movieItem?.let{
            setUpScreen(it.thumbnailUrl, it.Url, it.movieType)
        }

        detailViewModel.movieDetails.observe(viewLifecycleOwner, Observer {

        })

        detailViewModel.serverList.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->  dataHandler.handleServersLoaded(response)
                is Resource.Loading -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        detailViewModel.episodeList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> dataHandler.handleEpisodeLoaded(response)
                is Resource.Loading -> {
                    Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

        detailViewModel.vidEmbedLink.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> dataHandler.handleVidEmbedLinkLoaded(response)
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Network call being interrupted. Try using a VPN service or try from a different network", Toast.LENGTH_SHORT).show()
                }
            }
        })

        dataHandler.addListener(object : DetailHandlerListener{
            override fun onVideoUrlLoaded(url: String) {
                streamUrl = url
                loadPlayer(url)
            }

            override fun onServerItemSelected(pos: Int, server: ServerItem) {
                Toast.makeText(context, pos.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadPlayer(url: String) {
        if(url.isNotEmpty()){
            context?.let { mContext ->
                vidHelper = PlayerHelper(mContext, binding.vidPlayer)
                playerLoader = vidHelper.initPlayer()
                playerLoader.loadVideoandPlay(url)
            }
        }
    }

    fun setUpScreen(thumbnail: String, source: String, movieType: String){
        Glide.with(this).load(thumbnail)
            .centerCrop()
            .into(binding.ivMovieThumnail)


        detailViewModel.getMovieDetails(source)

        if (movieType == "TV") {
            detailViewModel.getSeasons(source)
        }

        if (movieType == "Movie") {
            detailViewModel.getMovieData(source)
        }

    }

    fun goFullScreen(){
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val fullScreenPlayerView = PlayerView(requireContext())
        val dialog = object : Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onBackPressed() {
                activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                PlayerView.switchTargetView(playerLoader.getPlayer(), fullScreenPlayerView, playerLoader.getPlayerView())
                super.onBackPressed()
            }
        }
        dialog.addContentView(
            fullScreenPlayerView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        dialog.show()
        PlayerView.switchTargetView(playerLoader.getPlayer(), playerLoader.getPlayerView(), fullScreenPlayerView)
    }

    override fun onResume() {
        super.onResume()
       // fullScreenActivity()
        loadPlayer(streamUrl)
    }

    override fun onPause() {
        super.onPause()
        if(this::vidHelper.isInitialized){
            vidHelper.releasePlayer()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(this::vidHelper.isInitialized){
            vidHelper.releasePlayer()
        }

        Log.d("onDestroyView", _binding.toString())
    }


    override fun onStop() {
        super.onStop()
        if(this::vidHelper.isInitialized){
            vidHelper.releasePlayer()
        }
    }

    private fun fullScreenActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity?.window?.setDecorFitsSystemWindows(false)
            val controller =  activity?.window?.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            activity?.window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

    }
}