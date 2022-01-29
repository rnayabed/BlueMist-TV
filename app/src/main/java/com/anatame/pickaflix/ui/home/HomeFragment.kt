package com.anatame.pickaflix.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.ui.home.adapter.HomeScreenAdapter
import com.anatame.pickaflix.utils.Resource
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialSharedAxis

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)
        setUpRecyclerView()

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.search -> {
                    navigateToSearch()
                }
            }
            true
        }

        homeViewModel.trendingMovies.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    binding.loadingIcon.show()
                }

                is Resource.Success -> {
                    binding.loadingIcon.hide()
                }

                is Resource.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    homeViewModel.getHomeScreenData()
                }
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
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
        holder: HomeScreenAdapter.ViewPagerViewHolder
    ) {
        homeTransition()

        val directions = HomeFragmentDirections.actionNavigationHomeToDetailFragment(cardView.transitionName)
        val extras = FragmentNavigatorExtras(cardView to cardView.transitionName)

        holder.itemView.findNavController().navigate(
            directions,
            extras
        )
    }

    fun navigateToDetailFromCategory(
        cardView: CardView,
        holder: HomeScreenAdapter.CategoryViewHolder
    ) {
        homeTransition()

        val directions = HomeFragmentDirections.actionNavigationHomeToDetailFragment(cardView.transitionName)
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

    fun setUpRecyclerView(){
        binding.RVHomeScreen.apply {
            adapter = HomeScreenAdapter(context, this@HomeFragment)
            layoutManager = LinearLayoutManager(context)
        }
    }
}