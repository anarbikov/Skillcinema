package com.skillcinema.ui.filmography

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFilmographyBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmographyViewModel by viewModels()
    private lateinit var chipGroup: ChipGroup
    private val chipList = mutableMapOf<Chip,String>()
    private lateinit var filmographyChippedAdapter: FilmographyChippedAdapter
    private lateinit var generalInfo: ActorGeneralInfoDto

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
 //       val staffId = 9144
        filmographyChippedAdapter = FilmographyChippedAdapter(requireContext())
        binding.chippedRecyclerView.adapter = filmographyChippedAdapter
        doObserveWork(staffId!!)
    }
    private fun doObserveWork(staffId:Int){
        viewModel.loadInitial(staffId)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.chippedRecyclerView .visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
                            binding.chippedRecyclerView .visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().nav_view.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.actorInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.films.receiveAsFlow().onEach {
            Log.d("mytag","receiverIt: ${it.size}")
            launchRecycler(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupAndRenderView (allInfo: List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        chipGroup = binding.chipGroup
        generalInfo = allInfo[1] as ActorGeneralInfoDto
        binding.actorNameTextView.text = generalInfo.nameRu ?: generalInfo.nameEn
        if (chipList.isEmpty()) {addChips(generalInfo)}
        binding.chippedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        startListener(generalInfo)
    }
    @SuppressLint("ResourceType", "SuspiciousIndentation")
    private fun startListener(generalInfo: ActorGeneralInfoDto){
        val currentStaffId = generalInfo.personId!!
            chipGroup.check(1)
            val chipInitial: Chip = chipGroup.findViewById(1)
                chipInitial.chipBackgroundColor =
                    ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
                viewModel.loadByChip(staffId = currentStaffId,chipList.getValue((chipGroup.findViewById(chipInitial.id))))
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            Log.d("mytag",checkedIds.toString())
            val chip:Chip? = if (checkedIds.isNotEmpty()) group.findViewById(checkedIds[0]) else null
            if (checkedIds.isNotEmpty()) {
                for (i in chipList.keys) {
                    i.chipBackgroundColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.grey))
                }
                chip!!.chipBackgroundColor =
                    ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
                viewModel.loadByChip(staffId = currentStaffId,chipList.getValue((chipGroup.findViewById(checkedIds[0]))))
            }
            else {
                for (i in chipList.keys)i.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey))
                filmographyChippedAdapter.removeData()
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun addChips(generalInfo:ActorGeneralInfoDto){
        val roles = mutableListOf<String>()
        for (role in generalInfo.films!!) role.professionKey?.let { roles.add(it) }
        val rolesDistincted = roles.distinct()
        var chipIdCounter = 1
        for (chipText in rolesDistincted) {
            val translatedText = when(chipText){
                "WRITER" -> requireContext().getString(R.string.WRITER)
                "OPERATOR" -> requireContext().getString(R.string.OPERATOR)
                "EDITOR" -> requireContext().getString(R.string.EDITOR)
                "COMPOSER" -> requireContext().getString(R.string.COMPOSER)
                "PRODUCER_USSR" -> requireContext().getString(R.string.PRODUCER_USSR)
                "HIMSELF" -> requireContext().getString(R.string.HIMSELF)
                "HERSELF" -> requireContext().getString(R.string.HERSELF)
                "HRONO_TITR_MALE" -> requireContext().getString(R.string.HRONO_TITR_MALE)
                "HRONO_TITR_FEMALE" -> requireContext().getString(R.string.HRONO_TITR_FEMALE)
                "TRANSLATOR" -> requireContext().getString(R.string.TRANSLATOR)
                "DIRECTOR" -> requireContext().getString(R.string.DIRECTOR)
                "DESIGN" -> requireContext().getString(R.string.DESIGN)
                "PRODUCER" -> requireContext().getString(R.string.PRODUCER)
                "ACTOR" -> requireContext().getString(R.string.ACTOR)
                "VOICE_DIRECTOR" -> requireContext().getString(R.string.VOICE_DIRECTOR)
                else -> requireContext().getString(R.string.UNKNOWN)
            }
            val filmsQuantity = generalInfo.films.filter{it.professionKey == chipText }.size
            val chip = Chip(requireContext())
            chip.text = "$translatedText $filmsQuantity"
            chip.id = chipIdCounter
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(
                requireContext().getColor(R.color.grey))
            chipList[chip] = chipText
            chipGroup.addView(chip)
            chipIdCounter ++
        }
    }
    @Suppress("UNCHECKED_CAST")
    private fun launchRecycler(allFilms: List<Any>){
        if (allFilms.isEmpty())return
        val isLoaded = allFilms[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else {binding.loadingErrorPage.visibility = View.GONE}
        val chippedFilms: List<FilmInfo> = allFilms[1] as List<FilmInfo>
        Log.d("mytag","chipped Films: ${chippedFilms.size}")
        filmographyChippedAdapter.removeData()
        filmographyChippedAdapter.addData(chippedFilms)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}