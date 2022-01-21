package com.anatame.pickaflix.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentHomeBinding
import com.anatame.pickaflix.ui.adapter.HomeScreenAdapter
import com.anatame.pickaflix.utils.Resource

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

        setUpRecylcerView()

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
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setUpRecylcerView(){
        binding.RVHomeScreen.apply {
            adapter = HomeScreenAdapter()
            layoutManager = LinearLayoutManager(context)
        }
    }
}