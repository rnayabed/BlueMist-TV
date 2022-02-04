package com.anatame.pickaflix.ui.views.bottomsheets

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.BottomsheetHomeBinding
import com.anatame.pickaflix.ui.detail.DetailFragmentArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetHomeBinding? = null
    private val binding get() = _binding!!
    private val args: HomeListDialogFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BottomsheetHomeBinding.inflate(inflater, container, false)

        binding.DeleteBtn.setOnClickListener{
            args.homeBottomSheetData?.let {
                it.homeFragment.removeFromWatchList(it.movie)
                dismiss()
            }
        }

        binding.DeleteAllBtn.setOnClickListener{
            args.homeBottomSheetData?.let {
                it.homeFragment.clearWatchList()
                dismiss()
            }
        }

        return binding.root
    }


}