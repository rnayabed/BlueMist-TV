package com.anatame.pickaflix.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.MainActivity
import com.anatame.pickaflix.R
import com.anatame.pickaflix.common.utils.HeadlessWebViewHelper
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.ui.home.HomeViewModel
import com.anatame.pickaflix.utils.Resource
import com.bumptech.glide.Glide
import com.google.android.material.transition.MaterialContainerTransform


class DetailFragment : Fragment() {
    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()
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

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

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

        args.heroItem?.let {
            setUpScreen(it.thumbnailUrl)
        }

        args.movieItem?.let{
            setUpScreen(it.thumbnailUrl)

            detailViewModel.getMovieDetails(it.Url)

            if (it.movieType == "TV") {
                detailViewModel.getSeasons(it.Url)
            }

            if (it.movieType == "Movie") {
                detailViewModel.getMovieData(it.Url)
            }

        }

        detailViewModel.vidEmbedLink.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    val webPlayer = (activity as MainActivity).getWebPlayer()
                    response.data?.let { it ->
                        getStreamUrl(webPlayer, it)
                    }
                }
            }
        })

    }

    private fun getStreamUrl(webPlayer: WebView, url: String) {
        val headlessWebViewHelper = HeadlessWebViewHelper(webPlayer, url)
        headlessWebViewHelper.setOnStreamUrlLoadedListener{
            Log.d("streamUrl", it)
        }
    }


    fun setUpScreen(thumbnail: String){
        Glide.with(this).load(thumbnail)
            .centerCrop()
            .into(binding.ivMovieThumnail)
    }
}