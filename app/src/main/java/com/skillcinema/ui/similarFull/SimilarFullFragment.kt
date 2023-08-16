package com.skillcinema.ui.similarFull

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.skillcinema.R
import com.skillcinema.databinding.FragmentSimilarFullBinding
import com.skillcinema.entity.FilmSimilarsDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SimilarFullFragment : Fragment() {

    private var _binding: FragmentSimilarFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SimilarFullViewModel by viewModels()
    private var kinopoiskId = 0
    private lateinit var similarFullAdapter: SimilarFullAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarFullBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kinopoiskId = arguments.let { it?.getInt("kinopoiskId")?:5260016 }
        binding.header.text = requireContext().getString(R.string.similar_header)
        doObserveWork()
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.recyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().nav_view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.movieInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @SuppressLint("SetTextI18n")
    private fun setupAndRenderView(allInfo:List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        val similar: FilmSimilarsDto = allInfo[1] as FilmSimilarsDto
        similarFullAdapter = SimilarFullAdapter(requireContext())
        binding.recyclerView.adapter = similarFullAdapter
        similarFullAdapter.addData(similar,kinopoiskId)
        binding.goUpButton.setOnClickListener{
            binding.recyclerView.scrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}