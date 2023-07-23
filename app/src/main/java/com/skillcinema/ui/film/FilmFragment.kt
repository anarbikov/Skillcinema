package com.skillcinema.ui.film

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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.databinding.FragmentFilmBinding
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmFragment : Fragment() {

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmViewModel by viewModels()
    private lateinit var generalInfoAdapter: FilmGeneralInfoAdapter
    private lateinit var filmSeasonsAdapter: FilmSeasonsAdapter
    private lateinit var filmActorsParentAdapter: FilmActorsParentAdapter
    private lateinit var filmGalleryParentAdapter: FilmGalleryParentAdapter
    private lateinit var filmSimilarParentAdapter: FilmSimilarParentAdapter
    private lateinit var concatAdapter: ConcatAdapter

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
        val kinopoiskId = arguments.let { it?.getInt("kinopoiskId")?:5260016 }
//        val kinopoiskId = 448
        setUpViews()
        doObserveWork(kinopoiskId)
    }
    private fun setUpViews(){
        generalInfoAdapter = FilmGeneralInfoAdapter(requireContext())
        filmSeasonsAdapter = FilmSeasonsAdapter(requireContext())
        filmActorsParentAdapter = FilmActorsParentAdapter(requireContext())
        filmGalleryParentAdapter = FilmGalleryParentAdapter(requireContext())
        filmSimilarParentAdapter = FilmSimilarParentAdapter(requireContext())
    }
    private fun doObserveWork(kinopoiskId:Int){
        viewModel.loadAll(kinopoiskId)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.concatRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
                            binding.concatRecyclerView.visibility = View.VISIBLE
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
    @Suppress("UNCHECKED_CAST")
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
        Log.d("mytag",allInfo.toString())
        val generalInfo: FilmInfo = allInfo[1] as FilmInfo
        val isSeries = generalInfo.serial
        generalInfoAdapter.addData(generalInfo)
        val seasons: FilmSeasonsDto = allInfo[2] as FilmSeasonsDto
        filmSeasonsAdapter.addData(seasons)
        val actorInfo: List<ActorDto> = allInfo[3] as List<ActorDto>
        val otherStaff: List<ActorDto> = allInfo[4] as List<ActorDto>
        filmActorsParentAdapter.addData(actorInfo,otherStaff, isSeries!!)
        val gallery: FilmGalleryDto = allInfo[5] as FilmGalleryDto
        filmGalleryParentAdapter.addData(gallery)
        val similar: FilmSimilarsDto = allInfo[6] as FilmSimilarsDto
        filmSimilarParentAdapter.addData(similar)
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, generalInfoAdapter,filmSeasonsAdapter,filmActorsParentAdapter,filmGalleryParentAdapter,filmSimilarParentAdapter)
        binding.concatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.concatRecyclerView.adapter = concatAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}