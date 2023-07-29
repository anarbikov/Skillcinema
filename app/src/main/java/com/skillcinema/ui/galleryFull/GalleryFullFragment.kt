package com.skillcinema.ui.galleryFull

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.skillcinema.databinding.FragmentGalleryFullBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFullFragment : Fragment() {

    private var _binding: FragmentGalleryFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryFullViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFullBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}