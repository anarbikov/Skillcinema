package com.skillcinema.ui.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.skillcinema.databinding.FragmentFilmBinding

class FilmFragment : Fragment() {

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments.let {it?.getString("posterUrlPreview")}
        Glide.with(requireView())
            .load(url)
            .centerCrop()
            .into(binding.posterImageView)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}