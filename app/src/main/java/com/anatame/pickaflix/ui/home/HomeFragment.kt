package com.anatame.pickaflix.ui.home

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Transition
import android.transition.TransitionListenerAdapter
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.anatame.pickaflix.model.scrollstate.HomeScrollStates
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.ui.home.adapter.rework.HomeItemRepo
import com.anatame.pickaflix.ui.home.adapter.rework.HomeScreenAdapter2
import com.anatame.pickaflix.ui.home.adapter.rework.ViewPagerItem
import com.anatame.pickaflix.ui.views.bottomsheets.HomeBottomSheetData
import com.anatame.pickaflix.utils.Resource
import com.anatame.pickaflix.utils.data.db.MovieDao
import com.anatame.pickaflix.utils.data.db.MovieDatabase
import com.anatame.pickaflix.utils.data.db.entities.Movie
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.HeroItem
import com.anatame.pickaflix.utils.data.remote.PageParser.Home.DTO.MovieItem
import com.anatame.pickaflix.utils.parser.Parser2
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeScreenAdapter: HomeScreenAdapter
    private lateinit var homeScreenAdapter2: HomeScreenAdapter2
    private lateinit var movieDao: MovieDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        movieDao = context?.let { MovieDatabase.invoke(it).getMovieDao() }!!
        val viewModelProviderFactory = HomeViewModelFactory(movieDao, Parser2)
        homeViewModel = ViewModelProvider(this, viewModelProviderFactory).get(HomeViewModel::class.java)

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
                    it.data?.let { item ->
                        setUpRecyclerView(item)

                    }
                }

                is Resource.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    homeViewModel.getHomeScreenData()
                }
            }
        })

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
        heroItem: HeroItem,
        pos: Int
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
                source = movieItem.Url,
                movieType = movieItem.movieType
            ))


                //doSomethingHere()
                homeViewModel.homeRvScrollState.postValue((binding.RVHomeScreen.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
                homeViewModel.updateHomeScreenData()

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
        val repo = HomeItemRepo()
        repo.addItem(ViewPagerItem())
        repo.addItem(ViewPagerItem())

        homeScreenAdapter2 = HomeScreenAdapter2(
            requireContext(),
            repo
        )

        binding.RVHomeScreen.apply {
            adapter = homeScreenAdapter2
            layoutManager = LinearLayoutManager(context)

            homeViewModel.homeRvScrollState.observe(viewLifecycleOwner, Observer{
                (binding.RVHomeScreen.layoutManager as LinearLayoutManager)
                    .scrollToPositionWithOffset(it, 200)
            })
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
        Handler(Looper.getMainLooper()).postDelayed({
            //doSomethingHere()
            homeViewModel.homeRvScrollState.postValue((binding.RVHomeScreen.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
        }, 300)
    }

    fun removeFromWatchList(movie: Movie) {
        lifecycleScope.launch(Dispatchers.IO){
            movieDao.deleteMovieFromWatchList(movie)
            homeViewModel.updateHomeScreenData()
        }
    }

}