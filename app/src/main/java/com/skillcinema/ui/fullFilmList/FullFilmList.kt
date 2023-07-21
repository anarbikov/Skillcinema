package com.skillcinema.ui.fullFilmList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFullFilmListBinding
import com.skillcinema.entity.Film
import com.skillcinema.ui.home.RecyclerItemDecoration
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FullFilmList : Fragment() {

    private var _binding: FragmentFullFilmListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FullFilmListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullFilmListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filterId = arguments.let { it?.getInt("filterId") }
        val filterDescription = arguments.let { it?.getString("description") }
        val pagedFilmAdapter =
            PagedFilmAdapter({ film: Film -> onItemClick(film,filterId!!) }, requireContext())
        super.onViewCreated(view, savedInstanceState)
        binding.categoryDescription.text = filterDescription
        binding.recyclerView.adapter = pagedFilmAdapter.withLoadStateFooter(MyLoadStateAdapter())
        binding.recyclerView.addItemDecoration(RecyclerItemDecoration(2, 5, includeEdge = true))
        viewModel.filterId = filterId!!
        viewModel.category = filterDescription!!
        when (filterId) {
            1111 -> {
                viewModel.pagedPremiere.onEach {
                    pagedFilmAdapter.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            2222 -> {viewModel.pagedPopular.onEach {
                pagedFilmAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
            3333 -> {viewModel.pagedSeries.onEach {
                pagedFilmAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            4444 -> {viewModel.pagedTop250.onEach {
                pagedFilmAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            else -> {viewModel.pagedRandom.onEach {
                pagedFilmAdapter.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
    }
        binding.goUpButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }
    }

    private fun onItemClick(item: Film, filterId: Int) {
        val bundle = Bundle()
        bundle.putString("posterUrlPreview", item.posterUrlPreview)
        bundle.putInt("kinopoiskId", item.kinopoiskId!!)
        findNavController().navigate(R.id.action_fullFilmList_to_filmFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}