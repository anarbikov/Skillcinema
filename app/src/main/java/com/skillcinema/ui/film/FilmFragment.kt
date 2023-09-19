package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFilmBinding
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.room.Film
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmFragment : Fragment() {

    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private var kinopoiskId = 0
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
        kinopoiskId = arguments.let { it?.getInt("kinopoiskId")?:5260016 }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
  //      kinopoiskId = 77044
        Log.d("mytag","dismissed")
        setUpViews()
        doObserveWork()

    }
    private fun setUpViews(){
        generalInfoAdapter = FilmGeneralInfoAdapter(
            requireContext(),
            {film -> onClickWatched(film = film)},
            {film -> onClickLiked(film = film)},
            {film -> onClickToWatch(film = film)},
            {film -> addToHistory(film) },
            {film -> addToCollection(film) }
        )
        filmSeasonsAdapter = FilmSeasonsAdapter(requireContext())
        filmActorsParentAdapter = FilmActorsParentAdapter(requireContext())
        filmGalleryParentAdapter = FilmGalleryParentAdapter(requireContext())
        filmSimilarParentAdapter = FilmSimilarParentAdapter(requireContext())
    }
    private fun doObserveWork() {
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
            setFragmentResultListener("update"){ _, _ ->
                Log.d("mytag","UPDATED!")
                viewModel.checkLiked()
                viewModel.checkWatched()
                viewModel.checkToWatch()
                setupAndRenderView(it)
            }
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
        viewModel.checkWatched()
        viewModel.checkLiked()
        viewModel.checkToWatch()
        val generalInfo: FilmInfo = allInfo[1] as FilmInfo
        val isSeries = generalInfo.serial
        generalInfoAdapter.addData(generalInfo)
        val seasons: FilmSeasonsDto = allInfo[2] as FilmSeasonsDto
        filmSeasonsAdapter.addData(seasons, kinopoiskId = kinopoiskId, filmName = (generalInfo.nameRu?:generalInfo.nameEn?:"") as String)
        val actorInfo: List<ActorDto> = allInfo[3] as List<ActorDto>
        val otherStaff: List<ActorDto> = allInfo[4] as List<ActorDto>
        filmActorsParentAdapter.addData(actorInfo,otherStaff, isSeries!!,generalInfo.kinopoiskId!!)
        val gallery: FilmGalleryDto = allInfo[5] as FilmGalleryDto
        filmGalleryParentAdapter.addData(gallery,generalInfo.kinopoiskId)
        val similar: FilmSimilarsDto = allInfo[6] as FilmSimilarsDto
        filmSimilarParentAdapter.addData(similar,generalInfo.kinopoiskId)
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, generalInfoAdapter,filmSeasonsAdapter,filmActorsParentAdapter,filmGalleryParentAdapter,filmSimilarParentAdapter)
        binding.concatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.concatRecyclerView.adapter = concatAdapter
    }
    private fun onClickWatched (film:Film) {
        if (film.isWatched == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = "watchedList")
        }else{
            viewModel.addFilmToCollection(collection = "watchedList", film = film )
        }
    }
    private fun onClickLiked (film:Film) {
        if (film.isLiked == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = "liked")
        }else {
            viewModel.addFilmToCollection(collection = "liked", film = film)
        }
    }
    private fun onClickToWatch(film: Film){
        if (film.toWatch == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = "toWatch")
        }else {
            viewModel.addFilmToCollection(collection = "toWatch", film = film)
        }
    }
    private fun addToHistory(film: Film){
        viewModel.addFilmToCollection("history",film = film)
    }
    private fun addToCollection(filmInfo: FilmInfo) {
        val bundle = bundleOf()
        bundle.putParcelable("filmInfo",filmInfo)
        childFragmentManager.setFragmentResult("key",bundle)
        findNavController().navigate(R.id.action_filmFragment_to_collectionDialogFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}