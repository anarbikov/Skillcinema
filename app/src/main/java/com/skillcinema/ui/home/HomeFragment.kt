package com.skillcinema.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.R
import com.skillcinema.entity.FilmsDto
import com.skillcinema.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.fragment_home.parentRecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    //    private var pagedPremiereAdapter = PagedFilmAdapter { film -> onItemClick(film) }
    private lateinit var parentFilmAdapter: ParentFilmAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkForOnboarding()
        if (viewModel.onboardingShownFlag == 0) findNavController().navigate(R.id.action_navigation_home_to_numberFragment)
        setUpViews()
        doObserveWork()
    }

    private fun setUpViews() {
        parentRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        parentFilmAdapter = ParentFilmAdapter(requireContext())
        parentRecyclerView.adapter = parentFilmAdapter

    }

    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.swipeRefresh.isRefreshing = false
                            binding.parentRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
                            binding.parentRecyclerView.visibility = View.VISIBLE
                            binding.swipeRefresh.isRefreshing = false
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().nav_view.visibility = View.VISIBLE
                        }
                    }
                }
            }
            }
        viewModel.movies.onEach {
            renderFilmsList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.isError.onEach {
            if (it) {
                binding.parentRecyclerView.visibility = View.GONE
                binding.loadingErrorPage.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                binding.button.setOnClickListener { requireActivity().finish() }
            }else {
                binding.loadingErrorPage.visibility = View.GONE
                binding.parentRecyclerView.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.loadingProgress.visibility = View.GONE
            requireActivity().nav_view.visibility = View.VISIBLE
            viewModel.movies.onEach {
                renderFilmsList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun renderFilmsList(films: List<FilmsDto>) {
        if (films.isEmpty())return
        viewModel.checkWatched()
        Log.d("mytag",films[3].items.toString())
        parentFilmAdapter.addData(films)
        parentFilmAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}