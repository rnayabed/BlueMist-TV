package com.anatame.pickaflix.ui.Detail

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.navArgs
import com.anatame.pickaflix.R
import com.anatame.pickaflix.databinding.FragmentDetailBinding
import com.anatame.pickaflix.databinding.FragmentDownloadsBinding
import com.google.android.material.transition.MaterialContainerTransform


class DetailFragment : Fragment() {
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
}