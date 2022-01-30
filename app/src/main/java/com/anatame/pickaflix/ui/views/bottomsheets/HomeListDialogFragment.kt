package com.anatame.pickaflix.ui.views.bottomsheets

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.BottomsheetHomeBinding

class HomeListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: BottomsheetHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BottomsheetHomeBinding.inflate(inflater, container, false)
        return binding.root

    }


}