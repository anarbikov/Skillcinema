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
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.skillcinema.databinding.FragmentFilmBinding
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.FilmInfo
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
    private lateinit var filmActorsAdapter: FilmActorsAdapter
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
        val url = arguments.let {it?.getString("posterUrlPreview")}
//        val kinopoiskId = 448
        doObserveWork(kinopoiskId)
    }
    private fun setUpViews(){

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
        generalInfoAdapter = FilmGeneralInfoAdapter(requireContext())
        val actorInfo: List<ActorDto> = allInfo[1] as List<ActorDto>
        val otherStaff: List<ActorDto> = allInfo[2] as List<ActorDto>
        filmActorsAdapter = FilmActorsAdapter(requireContext())
        filmActorsAdapter.addData(actorInfo,otherStaff)
        val generalInfo: FilmInfo = allInfo[0] as FilmInfo
        generalInfoAdapter.addData(generalInfo)
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, generalInfoAdapter,filmActorsAdapter)
        binding.concatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.concatRecyclerView.adapter = concatAdapter
        Log.d("mytag",allInfo.toString())






//        Glide.with(requireView())
//            .load(generalInfo.posterUrlPreview)
//            .centerCrop()
//            .into(binding.posterImageView)
//        val rating = generalInfo.ratingKinopoisk?:""
//        val filmName = generalInfo.nameRu ?: (generalInfo.nameOriginal?:"")
//        val filmYear = generalInfo.year?:""
//        val filmGenre = if (generalInfo.genres.isNotEmpty() && generalInfo.genres.size == 1 )", " + generalInfo.genres[0].genre
//        else (", " + generalInfo.genres[0].genre + ", " + generalInfo.genres[1].genre)
//        val filmCountries = if (generalInfo.countries.isNotEmpty() && generalInfo.countries.size == 1 ) generalInfo.countries[0].country
//        else generalInfo.countries[0].country+", "+generalInfo.countries[1].country
//        val durationHours = if(generalInfo.filmLength != null) (generalInfo.filmLength /60) else 0
//        val durationMinutes = if (generalInfo.filmLength != null) generalInfo.filmLength - durationHours * 60 else 0
//        val duration = if (generalInfo.filmLength != null) ", $durationHours ${getString(R.string.hour)} $durationMinutes ${getString(R.string.minutes)}" else ""
//        val ageRestriction =  if (generalInfo.ratingAgeLimits!=null) ", "+ generalInfo.ratingAgeLimits.toString().drop(3)+"+" else ""
//        binding.filmNameTextView.text = "$rating $filmName\n$filmYear$filmGenre\n$filmCountries$duration$ageRestriction"
//        binding.filmDescriptionHeaderTextView.text = if (generalInfo.shortDescription != null) generalInfo.shortDescription.toString() else ""
//        binding.filmDescriptionBodyTextView.text = if (generalInfo.description != null) generalInfo.description.toString() else ""
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}