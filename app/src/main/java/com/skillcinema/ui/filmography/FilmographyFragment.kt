package com.skillcinema.ui.filmography

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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFilmographyBinding
import com.skillcinema.entity.ActorGeneralInfoDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FilmographyFragment : Fragment() {

    private var _binding: FragmentFilmographyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilmographyViewModel by viewModels()
    private lateinit var chipGroup: ChipGroup
    private val chipList = mutableListOf<Chip>()

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
//        val staffId = arguments.let { it?.getInt("staffId") }
        val staffId = 9144

        setUpViews()
        doObserveWork(staffId!!)
    }
    private fun setUpViews(){
        chipGroup = binding.chipGroup
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            Log.d("mytag",checkedIds.toString())
            val chip:Chip? = if (checkedIds.isNotEmpty()) group.findViewById(checkedIds[0]) else null
            if (checkedIds.isNotEmpty()) {
                for (i in chipList) {
                    i.chipBackgroundColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.grey))
                }
                chip!!.chipBackgroundColor =
                    ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
            }
            else {
                for (i in chipList)i.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey))
            }

        }

    }
    private fun doObserveWork(staffId:Int){
        viewModel.loadAll(staffId)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
//                            binding.concatRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().nav_view.visibility = View.GONE
                        }

                        else -> {
//                            binding.concatRecyclerView.visibility = View.VISIBLE
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
    }

    private fun setupAndRenderView (allInfo:List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        val generalInfo: ActorGeneralInfoDto = allInfo[1] as ActorGeneralInfoDto
        binding.actorNameTextView.text = generalInfo.nameRu ?: generalInfo.nameEn
        val roles = mutableListOf<String>()
        for (role in generalInfo.films!!) role.professionKey?.let { roles.add(it) }
        val rolesDistincted = roles.distinct()

        for (chipText in rolesDistincted) {
            val chip = Chip(requireContext())
            chip.text = chipText
            chip.isCheckable = true
            chip.chipBackgroundColor = ColorStateList.valueOf(
                requireContext().getColor(R.color.grey))
            chipList.add(chip)
            chipGroup.addView(chip)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}