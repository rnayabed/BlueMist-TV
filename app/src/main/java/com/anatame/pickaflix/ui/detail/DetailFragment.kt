package com.anatame.pickaflix.ui.detail

import android.app.Dialog
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.detail.handler.dataHandlers.DetailHandlerListener
import com.anatame.pickaflix.utils.PlayerHelper
import com.anatame.pickaflix.utils.PlayerLoader
import com.anatame.pickaflix.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.transition.MaterialContainerTransform
import com.anatame.pickaflix.utils.parser.Parser3

import android.view.View
import android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
import android.view.WindowManager
import com.anatame.pickaflix.ui.detail.handler.dataHandlers.AbstractDetailHandler
import com.anatame.pickaflix.ui.detail.handler.dataHandlers.DetailHandlerImpl
import com.anatame.pickaflix.ui.detail.handler.uihandlers.AbstractDetailUiHandler
import com.anatame.pickaflix.ui.detail.handler.uihandlers.DetailUiHandlerImpl
import com.anatame.pickaflix.ui.detail.handler.uihandlers.DetailUiHandlerListener


class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var vidHelper: PlayerHelper
    private lateinit var playerLoader: PlayerLoader
    private var streamUrl = ""
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity() as MainActivity).hideBottomNav()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewModelFactory = DetailViewModelFactory(Parser3)
        detailViewModel = ViewModelProvider(this, viewModelFactory)[DetailViewModel::class.java]

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

        val detailUiHandler: AbstractDetailUiHandler = DetailUiHandlerImpl(
        binding,
        requireContext())

        val detailDataHandler: AbstractDetailHandler = DetailHandlerImpl((activity as MainActivity).getWebPlayer())

        var begin = System.currentTimeMillis()

        detailUiHandler.addListener(object : DetailUiHandlerListener {
            override fun seasonItemSelected(seasonDataID: String) {
                detailViewModel.getEpisodes(seasonDataID)
            }

            override fun episodeItemSelected(episodeDataID: String) {
                detailViewModel.getSelectedEpisodeVid(episodeDataID)
            }

            override fun videoEmbedLinkLoaded(embedUrl: String) {
                detailDataHandler.getStreamUrl(embedUrl)
                begin = System.currentTimeMillis()
            }

        })

        detailDataHandler.addListener(object : DetailHandlerListener{
            override fun onVideoUrlLoaded(url: String) {

                Log.d("totalLoadTimeTaken", (System.currentTimeMillis() - begin).toString())

                Toast.makeText(requireContext(), "Total time taken ${(System.currentTimeMillis() - begin)}", Toast.LENGTH_SHORT).show()

                if(this@DetailFragment::vidHelper.isInitialized){
                    vidHelper.releasePlayer()
                }
                streamUrl = url
                loadPlayer(url)

                binding.loadingIcon.hide()
                binding.progressBar.visibility = View.GONE
            }
        })



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

        detailViewModel.movieDetails.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> response.data?.let {
                    detailUiHandler.handleMovieDetailsLoaded(
                        it
                    )
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Network call being interrupted. Try using a VPN service or try from a different network", Toast.LENGTH_SHORT).show()
                }
            }
        })

        detailViewModel.seasonList.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> response.data?.let { detailUiHandler.handleSeasonsLoaded(it) }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Network call being interrupted. Try using a VPN service or try from a different network", Toast.LENGTH_SHORT).show()
                }
            }
        })

        detailViewModel.episodeList.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> response.data?.let { detailUiHandler.handleEpisodeLoaded(it) }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Network call being interrupted. Try using a VPN service or try from a different network", Toast.LENGTH_SHORT).show()
                }
            }
        })

        detailViewModel.vidEmbedLink.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> response.data?.let {
                    detailUiHandler.getVideoEmbedLinkLoaded(
                        it
                    )
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), "Network call being interrupted. Try using a VPN service or try from a different network", Toast.LENGTH_SHORT).show()
                }
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
        //
        if (movieType == "TV") {
            detailViewModel.getSeasons(source)
            binding.clTvControls.visibility = View.VISIBLE

        }

        if (movieType == "Movie") {
            detailViewModel.getMovieData(source)
            binding.clMovieControls.visibility = View.VISIBLE
        }
         Log.d("logit", source)

    }

    fun goFullScreen(){
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val fullScreenPlayerView = PlayerView(requireContext())
        dialog = object : Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                hideSystemUi()
            }

            override fun onStart() {
                super.onStart()
                hideSystemUi()
            }

            private fun hideSystemUi() {
                window!!
                    .setFlags(
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    )
                //Set the dialog to immersive sticky mode
                window!!.decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                or SYSTEM_UI_FLAG_HIDE_NAVIGATION)
                //Clear the not focusable flag from the window
                //Clear the not focusable flag from the window
                window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            }

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
        loadPlayer(streamUrl)

        if(this::dialog.isInitialized){
            dialog.window!!
                .setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                )
            //Set the dialog to immersive sticky mode
            dialog.window!!.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            //Clear the not focusable flag from the window
            //Clear the not focusable flag from the window
            dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }

        Log.d("onresume", "called")
    }

    override fun onStart() {
        super.onStart()
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