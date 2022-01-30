package com.anatame.pickaflix.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.model.HomeScreenData
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.views.bottomsheets.HomeBottomSheetData
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.data.db.MovieDatabase
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeScreenAdapter: HomeScreenAdapter
    private lateinit var movieDao: MovieDao

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        movieDao = context?.let { MovieDatabase.invoke(it).getMovieDao() }!!
        val viewModelProviderFactory = HomeViewModelFactory(movieDao)
        homeViewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

        homeViewModel.updateHomeScreenData()
        homeViewModel.setFirstInit()

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.search -> {
                    navigateToSearch()
                }
            }
            true
        }

        homeViewModel.homeScreenData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    binding.loadingIcon.show()
                }

                is Resource.Success -> {
                    binding.loadingIcon.hide()
                    it.data?.let { item -> setUpRecyclerView(item) }
                }

                is Resource.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    homeViewModel.getHomeScreenData()
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun navigateToSearch(){
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 300
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 300
        }

        val destination = HomeFragmentDirections.actionNavigationHomeToSearchFragment()
        findNavController().navigate(destination)
    }

    fun navigateToDetailFromHero(
        cardView: CardView,
        holder: HomeScreenAdapter.ViewPagerViewHolder,
        heroItem: HeroItem
    ) {
        homeTransition()

        val directions = HomeFragmentDirections.actionNavigationHomeToDetailFragment(
            cardView.transitionName,
            null,
            heroItem,
            null,
            null
        )
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)

        holder.itemView.findNavController().navigate(
            directions,
            extras
        )
    }

    fun navigateToDetailFromCategory(
        cardView: CardView,
        holder: HomeScreenAdapter.CategoryViewHolder,
        movieItem: MovieItem
    ) {
        homeTransition()

        lifecycleScope.launch(Dispatchers.IO) {
            movieDao.upsert(Movie(
                title = movieItem.title,
                thumbnailUrl = movieItem.thumbnailUrl,
                source = movieItem.Url
            ))
        }

        val directions = HomeFragmentDirections.actionNavigationHomeToDetailFragment(
            cardView.transitionName,
            movieItem,
            null,
            null,
            null
        )
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)

        holder.itemView.findNavController().navigate(
            directions,
            extras
        )
    }

    fun navigateToDetailFromWatchList(
        cardView: CardView,
        holder: HomeScreenAdapter.CategoryViewHolder,
        movie: Movie
    ) {
        homeTransition()

        val directions = HomeFragmentDirections.actionNavigationHomeToDetailFragment(
            cardView.transitionName,
            null,
            null,
            null,
            movie
        )
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)

        holder.itemView.findNavController().navigate(
            directions,
            extras
        )
    }

    fun homeTransition(){
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }
    }

    fun setUpRecyclerView(homeScreenData: HomeScreenData){
        binding.RVHomeScreen.apply {
            homeScreenAdapter = HomeScreenAdapter(context, this@HomeFragment, homeScreenData)
            adapter = homeScreenAdapter
            layoutManager = LinearLayoutManager(context)
        }

    }

    fun handleWatchListLongClick(cardView: CardView, holder: HomeScreenAdapter.CategoryViewHolder, data: Movie) {
        val destination = HomeFragmentDirections.actionNavigationHomeToHomeListDialogFragment(
            HomeBottomSheetData(
                this@HomeFragment,
                data
            )
        )
        findNavController().navigate(destination)
    }

    fun removeFromWatchList(movie: Movie) {
        lifecycleScope.launch(Dispatchers.IO){
            movieDao.deleteMovieFromWatchList(movie)
            homeViewModel.updateHomeScreenData()
        }
    }

}