package com.skillcinema.ui.filmography

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
import com.skillcinema.databinding.FragmentFilmographyBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmographyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmographyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val staffId = arguments.let { it?.getInt("staffId") }
        Log.d("mytag","Filmography: ${staffId.toString()}")
        setUpViews()
        doObserveWork(staffId!!)
    }
    private fun doObserveWork(staffId:Int){
        viewModel.loadAll(staffId)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
//                    when (it) {
//                        true -> {
//                            binding.concatRecyclerView.visibility = View.GONE
//                            binding.loadingProgress.visibility = View.VISIBLE
//                            requireActivity().nav_view.visibility = View.GONE
//                        }
//
//                        else -> {
//                            binding.concatRecyclerView.visibility = View.VISIBLE
//                            binding.loadingProgress.visibility = View.GONE
//                            requireActivity().nav_view.visibility = View.VISIBLE
//                        }
//                    }
                }
            }
        }
        viewModel.actorInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupAndRenderView (allInfo:List<Any>){
        if (allInfo.isEmpty())return



    }
    private fun setUpViews(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}